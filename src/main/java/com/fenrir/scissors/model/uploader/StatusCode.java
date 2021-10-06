/**
 * Copyright 2014 DV8FromTheWorld (Austin Keener)
 * Modifications copyright 2021 Fenrir7734
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fenrir.scissors.model.uploader;

public enum StatusCode {
    OK("Image uploaded successfully", 200),
    BAD_REQUEST("Incorrect parameter or image corrupted", 400),
    UNAUTHORIZED("Invalid OAuth credentials", 401),
    FORBIDDEN("You can't perform this action. You may have run out of API credits or your token is invalid",
            403),
    NOT_FOUND("Resource does not exist", 404),
    RATE_LIMIT("You hit rate limiting", 429),
    INTERNAL_ERROR("Unexpected internal error", 500),
    UNKNOWN_ERROR("An unknown error occurred", -1);

    private final int code;
    private final String description;

    StatusCode(String description, int code) {
        this.description = description;
        this.code = code;
    }

    public static StatusCode getResponseCode(int code) {
        return switch (code) {
            case 200 -> OK;
            case 400 -> BAD_REQUEST;
            case 401 -> UNAUTHORIZED;
            case 403 -> FORBIDDEN;
            case 404 -> NOT_FOUND;
            case 429 -> RATE_LIMIT;
            case 500 -> INTERNAL_ERROR;
            default -> UNKNOWN_ERROR;
        };
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }
}
