package Fitnesse.agent;

import java.util.concurrent.*;

import jetbrains.buildServer.*;
import jetbrains.buildServer.agent.*;
import org.jetbrains.annotations.*;

public abstract class FutureBasedBuildProcess implements BuildProcess, Callable<BuildFinishedStatus> {
    private Future<BuildFinishedStatus> myFuture;

    public void start() throws RunBuildException {
        try {
            myFuture = Executors.newSingleThreadExecutor().submit(this);
        }
        catch (final RejectedExecutionException e) {
            throw new RunBuildException(e);
        }
    }

    public boolean isInterrupted() {
        return myFuture.isCancelled() && isFinished();
    }

    public boolean isFinished() {
        return myFuture.isDone();
    }

    public void interrupt() {
        myFuture.cancel(true);
    }

    @NotNull
    public BuildFinishedStatus waitFor() throws RunBuildException {
        try {
            final BuildFinishedStatus status = myFuture.get();
            return status;
        }
        catch (final InterruptedException e) {
            throw new RunBuildException(e);
        }
        catch (final ExecutionException e) {
            throw new RunBuildException(e);
        }
        catch (final CancellationException e) {
            return BuildFinishedStatus.INTERRUPTED;
        }
    }
}
