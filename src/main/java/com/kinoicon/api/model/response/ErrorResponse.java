package com.kinoicon.api.model.response;

public record ErrorResponse(Integer errorCode, String message, String moreInfo, Object usefullData) {}
