#include "pch.h"
#include "CloudMusicUtils.h"
#include <iostream>
#include <Psapi.h>
HWND hwnd = NULL;
DWORD processID = NULL;
HANDLE processHandle = NULL;
DWORD_PTR offsetAddress = NULL;
/*****************************************************************
 *wchar_t* ----->jstring
 *******************************************************************/
jstring w2js(JNIEnv* env, wchar_t* str)
{
	size_t len = wcslen(str);
	jchar* str2 = (jchar*)malloc(sizeof(jchar) * (len + 1));
	size_t i;
	for (i = 0; i < len; i++)
		str2[i] = str[i];
	str2[len] = 0;
	jstring js = env->NewString(str2, len);
	free(str2);
	return js;
}

/*
 * Class:     CloudMusicUtils
 * Method:    initNativeDll
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_CloudMusicUtils_initNativeDll
(JNIEnv*, jclass)
{
	hwnd = FindWindow(L"OrpheusBrowserHost", NULL);
	if (hwnd == NULL)
	{
		return JNI_FALSE;
	}

	GetWindowThreadProcessId(hwnd, &processID);
	if (processID == NULL)
	{
		return JNI_FALSE;
	}

	DWORD_PTR baseAddress = 0;
	processHandle = OpenProcess(PROCESS_ALL_ACCESS, FALSE, processID);
	HMODULE* moduleArray;
	LPBYTE moduleArrayBytes;
	DWORD bytesRequired;
	if (processHandle)
	{
		if (EnumProcessModules(processHandle, NULL, 0, &bytesRequired))
		{
			if (bytesRequired)
			{
				moduleArrayBytes = (LPBYTE)LocalAlloc(LPTR, bytesRequired);
				if (moduleArrayBytes)
				{
					unsigned int moduleCount;
					moduleCount = bytesRequired / sizeof(HMODULE);
					moduleArray = (HMODULE*)moduleArrayBytes;
					if (EnumProcessModules(processHandle, moduleArray, bytesRequired, &bytesRequired))
					{
						wchar_t moduleName[256];
						for (unsigned int i = 0; i < moduleCount; i++)
						{
							GetModuleBaseName(processHandle, moduleArray[i], moduleName, 256);
							if (std::wcscmp(moduleName, L"cloudmusic.dll") == 0)
							{
								baseAddress = (DWORD_PTR)moduleArray[i];
								break;
							}
						}
					}
					LocalFree(moduleArrayBytes);
				}
			}
		}
	}
	else
	{
		return JNI_FALSE;
	}

	if (baseAddress != 0)
	{
		offsetAddress = 0x8669C8 + baseAddress;
	}
	else
	{
		return JNI_FALSE;
	}
	return JNI_TRUE;
}

/*
 * Class:     CloudMusicUtils
 * Method:    getCloudMusicTitle
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_CloudMusicUtils_getCloudMusicTitle
(JNIEnv* env, jclass)
{
	wchar_t titleStr[256];
	GetWindowText(hwnd, titleStr, 256);
	return w2js(env, titleStr);
}

/*
 * Class:     CloudMusicUtils
 * Method:    getCurrentPosition
 * Signature: (JI)D
 */
JNIEXPORT jdouble JNICALL Java_CloudMusicUtils_getCurrentPosition
(JNIEnv*, jclass)
{
	//HANDLE processHandle = OpenProcess(PROCESS_ALL_ACCESS, FALSE, processID);
	double position;
	SIZE_T outNum;
	ReadProcessMemory(processHandle, (LPCVOID)offsetAddress, (LPVOID)(&position), 8, &outNum);
	return position;
}

/*
/*
 * Class:     CloudMusicUtils
 * Method:    nativeGc
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_CloudMusicUtils_nativeGc
(JNIEnv*, jclass)
{
	if (processHandle != NULL)
		CloseHandle(processHandle);
}
