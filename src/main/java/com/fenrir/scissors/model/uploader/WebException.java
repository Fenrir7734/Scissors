/**
 * Copyright 2014 DV8FromTheWorld (Austin Keener)
 * Modifications copyright 2021 Fenrir7734 (Karol Hetman)
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

public class WebException extends Exception {

    private StatusCode code;

    public WebException(StatusCode code) {
        super(code.getDescription());
        this.code = code;
    }

    public StatusCode getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return "Status code " + code.getCode() + ": " + code.getDescription();
    }
}
