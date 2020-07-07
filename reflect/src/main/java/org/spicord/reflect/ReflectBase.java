/*
 * Copyright (C) 2020  OopsieWoopsie
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.spicord.reflect;

@SuppressWarnings("unchecked")
public class ReflectBase<T> implements ExceptionHandler {

    private ReflectErrorRule errorRule = ReflectErrorRule.THROW;
    private ReflectExceptionHandler errorHandler;

    public T setErrorRule(ReflectErrorRule errorRule) {
        this.errorRule = errorRule;
        return (T) this;
    }

    public ReflectErrorRule getErrorRule() {
        return errorRule;
    }

    public T setErrorHandler(ReflectExceptionHandler handler) {
        this.errorHandler = handler;
        return (T) this;
    }

    @Override
    /*protected*/ public void handleException(Exception e) {
        switch (getErrorRule()) {
        case IGNORE:
            break;
        case PRINT:
            e.printStackTrace(System.err);
            break;
        case THROW:
            if (errorHandler == null) {
                throw new ReflectException(e);
            } else {
                errorHandler.handle(e);
            }
        }
    }
}
