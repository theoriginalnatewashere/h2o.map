package com.bluewater.cleanwatermap

class IntentDataKeyIntentNotFoundException constructor(missingKey: String): Exception("Missing data intent key ${missingKey}")