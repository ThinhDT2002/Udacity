package com.udacity.webcrawler.profiler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * A method interceptor that checks whether {@link Method}s are annotated with the {@link Profiled}
 * annotation. If they are, the method interceptor records how long the method invocation took.
 */
final class ProfilingMethodInterceptor implements InvocationHandler {

  private final Clock clock;
  private final Object targetObject;
  private final ProfilingState profilingState;
  ProfilingMethodInterceptor(Clock clock, Object targetObject, ProfilingState profilingState) {
    this.clock = Objects.requireNonNull(clock);
    this.targetObject = targetObject;
    this.profilingState = profilingState;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Instant time = clock.instant();
    try {
      return method.invoke(this.targetObject, args);
    } catch (InvocationTargetException e) {
      throw e.getTargetException();
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } finally {
      Duration duration = Duration.between(time, clock.instant());
      profilingState.record(targetObject.getClass(), method, duration);
    }
  }
}
