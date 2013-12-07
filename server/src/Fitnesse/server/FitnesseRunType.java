/*
 * Copyright 2000-2012 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package Fitnesse.server;

import Fitnesse.common.Util;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FitnesseRunType extends RunType{

    private final Map<String, String> _defaultProperties = new HashMap<String, String>();


    public FitnesseRunType(final RunTypeRegistry runTypeRegistry) {
        runTypeRegistry.registerRunType(this);
    }

    @NotNull
    @Override
    public String getType() {
        return Util.RUNNER_TYPE;
    }

    @Override
    public String getDisplayName() {
        return "Fitnesse runner";
    }

    @Override
    public String getDescription() {
        return "Description of Fitnesse";
    }

    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor() {
        return new FitnessePropertiesProcessor();
    }

    @Override
    public String getEditRunnerParamsJspFilePath() {
        return "editFitnesse.jsp";
    }

    @Override
    public String getViewRunnerParamsJspFilePath() {
        return "viewFitnesse.jsp";
    }

    @Override
    public Map<String, String> getDefaultRunnerProperties() {
        return _defaultProperties ;
    }
}
