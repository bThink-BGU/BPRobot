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
package il.ac.bgu.cs.bp.bprobot.remote.command;


import il.ac.bgu.cs.bp.bprobot.remote.model.CommandType;

import java.util.Map;

/**
 * The simplest implementation of {@link CommandBase}.
 */
public class Command extends CommandBase {
    public Command(CommandType type, Map<String, Object> args) {
        super(type, args);
    }
}
