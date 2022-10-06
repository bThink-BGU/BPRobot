/**
 * Copyright (C) 2011-2017 The PILE Developers <pile-dev@googlegroups.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package il.ac.bgu.cs.bp.bprobot.remote.model;

/**
 * An enum class which contains the types of {@link Command}s.
 */
public enum CommandType {
    SET_LED_ON,
    SET_LED_OFF,
    SET_MOTOR_SPEED,
    SET_SERVO_ANGLE,
    GET_SERVO_ANGLE,
    SET_BUZZER_ON,
    SET_BUZZER_OFF,
    SET_BUZZER_BEEP,
    GET_LIGHT_VALUE,
    GET_GYRO_ANGLE,
    GET_GYRO_RATE,
    GET_TOUCH_TOUCHED,
    GET_TOUCH_COUNT,
    GET_COLOR_RGB,
    GET_COLOR_ILLUMINANCE,
    GET_RANGEFINDER_DIST,
    GET_REMOTECONTROLLER_BUTTON,
    GET_REMOTECONTROLLER_DIST,
    GET_SOUND_DB,
    GET_SI_VALUES,
    GET_PERCENT_VALUES,
    GET_SYSTEM_TYPE_MODE;
}
