#include "windows.h"
//#include "stdhdrs.h"
#define _JNI_IMPLEMENTATION_
#include "jni.h"

/*
 * 19-09-03 - Created
 * 22-09-03 - Modified the exported method to have  a lower case 'c' in create
 *            made the trace only appear if compiled -DDEBUG
 *
 *            compiled cl -c swtprint.cpp
 *            link /DLL swtprint.obj user32.lib gdi32.lib  
 *            rename swtprint.nodeb to swtprint.dll to use
 *
 *            compiled cl -c -DDEBUG -Zi -Od swtprint.cpp
 *            link /DLL /DEBUG swtprint.obj user32.lib gdi32.lib
 *            rename swtprint.debug to swtprint.dll to use
 *  
 *            Modify package to be swt.targetvm
 *            Fix bitmap size
 */
 
extern "C" JNIEXPORT jint JNICALL       
     JNI_OnLoad(JavaVM *vm, void *reserved)
{

#ifdef DEBUG
    printf("JNI_Onload of dll swtprint was called\n");
    printf("The exported method is called Java_com_ibm_etools_jbcf_swt_targetvm_ImageGrabber_createPrintedPixels\n");
#endif

    return JNI_VERSION_1_2;
}


extern "C" JNIEXPORT jintArray Java_com_ibm_etools_jbcf_swt_targetvm_ImageGrabber_createPrintedPixels(
    JNIEnv *env, jobject self,jint hwnd, jint wid, jint height) {
 
    
    HWND h = (HWND)hwnd;

#ifdef DEBUG
    printf("CreatePrintedPixels called with hwnd %p, width %ld, height %ld\n", h,wid,height);
#endif

    if (!::IsWindowVisible(h)) {
        
#ifdef DEBUG
        printf("CreatePrintedPixels returns false - IsWindowVisible failed\n");
#endif

        return NULL;
    }
 
    SIZE s;
    RECT r;
    ::GetWindowRect(h,&r);

    s.cx = (int)wid;
    s.cy = (int)height;

    HDC hdc = (HDC)::GetDC(h);
    HDC hMemoryDC = ::CreateCompatibleDC(hdc);
    HBITMAP hBitmap = ::CreateCompatibleBitmap(hdc, r.right-r.left, r.bottom-r.top);

#ifdef DEBUG
        printf("CreatePrintedPixels created compatible bitmap to window size %ld by %ld\n", r.right-r.left, r.bottom-r.top);
#endif
    
    HDC hStretchedMemoryDC = ::CreateCompatibleDC(hdc);
    HBITMAP hStretchedBitmap = ::CreateCompatibleBitmap(hdc, s.cx,s.cy);

#ifdef DEBUG
        printf("CreatePrintedPixels created stretched bitmap to ordered size %ld by %ld\n", s.cx, s.cy);
#endif
    
    HBITMAP hOldBitmap = (HBITMAP)::SelectObject(hMemoryDC, hBitmap);
    
    ::ReleaseDC(h,hdc);
 
    //RECT eraseR = { 0, 0, s.cx, s.cy };
    //::FillRect(hMemoryDC, &eraseR, (HBRUSH)0);
 
    ::SetWindowOrgEx(hMemoryDC, 0,0, NULL);
 
    // Don't bother with PRF_CHECKVISIBLE because we called IsWindowVisible
    // above.
    
    SendMessage(h,WM_PRINT, (WPARAM)hMemoryDC, PRF_CLIENT | PRF_NONCLIENT | PRF_CHILDREN | PRF_ERASEBKGND );
 
 
    ::StretchBlt( hStretchedMemoryDC,0,0,r.right-r.left, r.bottom-r.top, 
                  hMemoryDC,0,0,s.cx,s.cy, SRCCOPY);
  
    ::SelectObject(hMemoryDC, hOldBitmap);
    
#ifdef DEBUG
        printf("CreatePrintedPixels stretchBlt from original to stretched\n");
#endif

    BITMAPINFO bmi;
    memset(&bmi, 0, sizeof(BITMAPINFO));
    bmi.bmiHeader.biSize = sizeof(BITMAPINFOHEADER);
    bmi.bmiHeader.biWidth = s.cx;
    bmi.bmiHeader.biHeight = -s.cy;
    bmi.bmiHeader.biPlanes = 1;
    bmi.bmiHeader.biBitCount = 32;
    bmi.bmiHeader.biCompression = BI_RGB;
    
#ifdef DEBUG
    printf("CreatePrintedPixels = Create a new pixel array...\n");
#endif
    
    jobject localPixelArray = env->NewIntArray(s.cx * s.cy);
    
    jintArray pixelArray = NULL;
    if (localPixelArray != NULL) {
        pixelArray = (jintArray)env->NewGlobalRef(localPixelArray);
        env->DeleteLocalRef(localPixelArray); localPixelArray = NULL;
 
        jboolean isCopy;
        jint *pixels = env->GetIntArrayElements(pixelArray, &isCopy);
 
//        ::GetDIBits(hStretchedMemoryDC, hStretchedBitmap, 0, s.cy, (LPVOID)pixels, &bmi,
//                    DIB_RGB_COLORS);
        ::GetDIBits(hMemoryDC, hBitmap, 0, s.cy, (LPVOID)pixels, &bmi,
                    DIB_RGB_COLORS);
 

        env->ReleaseIntArrayElements(pixelArray, pixels, 0);
    }
    
    ::DeleteObject(hStretchedBitmap);
    ::DeleteDC(hStretchedMemoryDC);
 
    ::DeleteObject(hBitmap);
    ::DeleteDC(hMemoryDC);
    
#ifdef DEBUG
    printf("CreatePrintedPixels - Return a pixel array to java...\n");
#endif
    
    return pixelArray;
}
 
