package com.udacity.webcrawler.profiler;

import javax.inject.Inject;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Path;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Objects;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;

/**
 * Concrete implementation of the {@link Profiler}.
 */
final class ProfilerImpl implements Profiler {

    private final Clock clock;
    private final ProfilingState state = new ProfilingState();
    private final ZonedDateTime startTime;

    @Inject
    ProfilerImpl(Clock clock) {
        this.clock = Objects.requireNonNull(clock);
        this.startTime = ZonedDateTime.now(clock);
    }

    @Profiled
    private boolean isMethodAnnotatedProfile(Class<?> klass) {
        Method[] methods = klass.getMethods();
        for (Method method : methods) {
            Profiled profiled = method.getAnnotation(Profiled.class);
            if (profiled != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T> T wrap(Class<T> klass, T delegate) {
        Objects.requireNonNull(klass);
        if(!isMethodAnnotatedProfile(klass)) {
            throw new IllegalArgumentException();
        }
        ProfilingMethodInterceptor profilingMethodInterceptor = new ProfilingMethodInterceptor(clock, delegate, state);
        Object proxy = Proxy.newProxyInstance(
                ProfilerImpl.class.getClassLoader(),
                new Class[]{klass},
                profilingMethodInterceptor
        );
        return (T) proxy;
    }

    @Override
    public void writeData(Path path) {
        try(FileWriter fileWriter = new FileWriter(path.toFile(), true)) {
            writeData(fileWriter);
        } catch (IOException ioEx) {
            System.err.println(ioEx.getMessage());
        }
    }

    @Override
    public void writeData(Writer writer) throws IOException {
        writer.write("Run at " + RFC_1123_DATE_TIME.format(startTime));
        writer.write(System.lineSeparator());
        state.write(writer);
        writer.write(System.lineSeparator());
    }
}
