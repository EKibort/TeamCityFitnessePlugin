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

package Fitnesse.agent;

import com.intellij.execution.util.EnvironmentVariable;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: Advard
 * Date: 31.03.12
 * Time: 23:33
 * To change this template use File | Settings | File Templates.
 */
public class FitnesseProcess extends  FutureBasedBuildProcess {
    @NotNull
    private final AgentRunningBuild Build;
    @NotNull
    private final BuildRunnerContext Context;
    @NotNull
    private final BuildProgressLogger Logger;

    public FitnesseProcess (@NotNull final AgentRunningBuild build, @NotNull final BuildRunnerContext context)
    {
        Build = build;
        Context = context;
        Logger = build.getBuildLogger();
    }

    private String getParameter(@NotNull final String parameterName)
    {
        final String value = Context.getRunnerParameters().get(parameterName);
        if (value == null || value.trim().length() == 0) return null;
        String result = value.trim();
        return result;
    }

    private String[] getFitnesseCmd()
    {
        File jarFitnesse = new File("D:\\WORK\\fitnesse\\fitnesse.jar");
        File rootFitnesse = new File(jarFitnesse.getParent());

        String[] result =
                {"java",
                 "-jar",jarFitnesse.getAbsolutePath(),
                 "-d", rootFitnesse.getAbsolutePath() , //TODO Adbs path
                 "-r", "FitNesseRoot",
                 "-p", "8081",
        };

        return result;
    }

    private Process runFitnesseInstance()
    {
        try
        {
            String[] cmdFitnesse = getFitnesseCmd();
            File rootFitnesse = new File("D:\\WORK\\fitnesse");

            return Runtime.getRuntime().exec(cmdFitnesse);
        }
        catch (IOException e) {
            Logger.exception(e);
        }
        return null;
    }




    @NotNull
    public BuildFinishedStatus call() throws Exception
    {
        try
        {
            //Runtime.getRuntime().exec("");

            Process fitProcess = runFitnesseInstance();
            Logger.progressMessage("Fitnesse runned");


            //logger.progressMessage("ZipPath: " + output);
            //throw new RunBuildException("The Folder path provided is not a folder");

            Thread.sleep(5000);
            Logger.progressMessage("terminating");

            fitProcess.destroy();

            return BuildFinishedStatus.FINISHED_SUCCESS;
        }
        catch (Exception e)
        {
            return BuildFinishedStatus.FINISHED_FAILED;
        }
    }
}
