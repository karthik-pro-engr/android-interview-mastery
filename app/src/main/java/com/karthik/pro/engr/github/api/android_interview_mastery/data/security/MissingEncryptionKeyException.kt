package com.karthik.pro.engr.github.api.android_interview_mastery.data.security

import java.security.GeneralSecurityException

class MissingEncryptionKeyException :
    GeneralSecurityException("Token encryption key is missing")