#include "pch.h"
#include "CloudMusicUtils.h"
#include "JNIUtils.h"
#include "CloudMusicImpl.h"
//auto_ptr
std::shared_ptr<IBGMProtocol> cloudMusicUtil = std::make_shared<CloudMusicImpl>();
/*
 * Class:     CloudMusicUtils
 * Method:    initNativeDll
 * Signature: ()Z
 */
inline JNIEXPORT jboolean JNICALL Java_CloudMusicUtils_initNativeDll
(JNIEnv*, jclass)
{
	return cloudMusicUtil->initNativeProtocol() ? JNI_TRUE : JNI_FALSE;
}

/*
 * Class:     CloudMusicUtils
 * Method:    getCloudMusicTitle
 * Signature: (J)Ljava/lang/String;
 */
inline JNIEXPORT jstring JNICALL Java_CloudMusicUtils_getCloudMusicTitle
(JNIEnv* env, jclass)
{
	std::wstring title = cloudMusicUtil->getTitle();
	return JNIUtils::w2js(env, title);
}

/*
 * Class:     CloudMusicUtils
 * Method:    getCurrentPosition
 * Signature: (JI)D
 */
inline JNIEXPORT jdouble JNICALL Java_CloudMusicUtils_getCurrentPosition
(JNIEnv*, jclass)
{
	return cloudMusicUtil->getPosition();
}
/*
 * Class:     CloudMusicUtils
 * Method:    nativeGc
 * Signature: ()V
 */
inline JNIEXPORT void JNICALL Java_CloudMusicUtils_nativeGc
(JNIEnv*, jclass)
{
	cloudMusicUtil->nativeGc();
}
