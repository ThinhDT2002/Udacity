package com.udacity.catpoint.security.service;

import com.udacity.catpoint.image.service.FakeImageService;
import com.udacity.catpoint.security.application.StatusListener;
import com.udacity.catpoint.security.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceTest {
    private SecurityService securityService;
    private Sensor sensor;
    private final String uuid = UUID.randomUUID().toString();
    @Mock
    private SecurityRepository securityRepository;
    @Mock
    private FakeImageService fakeImageService;
    @Mock
    private StatusListener statusListener;
    private Set<Sensor> getAllSensors(int count) {
        Set<Sensor> sensors = new HashSet<>();
        while (count > 0) {
            sensors.add(new Sensor(uuid, SensorType.DOOR));
            count--;
        }
        sensors.forEach(sensor -> sensor.setActive(true));
        return sensors;
    }

    @BeforeEach
    void setUp() {
        sensor = new Sensor(uuid, SensorType.DOOR);
        securityService = new SecurityService(securityRepository, fakeImageService);
    }

    @Test
    @DisplayName("If alarm is armed and a sensor becomes activated, put the system into pending alarm status")
    void ifAlarmArmedAndSensorActivated_putStatusIntoPending() {
        when(securityRepository.getArmingStatus()).thenReturn(ArmingStatus.ARMED_HOME);
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.NO_ALARM);
        securityService.changeSensorActivationStatus(sensor, true);
        verify(securityRepository, times(1)).setAlarmStatus(AlarmStatus.PENDING_ALARM);
        verify(securityRepository, times(1)).updateSensor(sensor);
    }

    @Test
    @DisplayName("If alarm is armed and a sensor becomes activated and the system is already pending alarm, " +
            "set the alarm status to alarm")
    void ifAlarmArmedAndSensorActivatedAndPendingAlarm_setStatusToAlarm() {
        when(securityRepository.getArmingStatus()).thenReturn(ArmingStatus.ARMED_HOME);
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.PENDING_ALARM);
        securityService.changeSensorActivationStatus(sensor, true);
        verify(securityRepository, times(1)).setAlarmStatus(AlarmStatus.ALARM);
        verify(securityRepository, times(1)).updateSensor(sensor);
    }

    @Test
    @DisplayName("If pending alarm and all sensors are inactive, return to no alarm state")
    void ifPendingAlarmAndAllSensorsInactive_returnToNoAlarmState() {
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.PENDING_ALARM);
        sensor.setActive(false);
        securityService.changeSensorActivationStatus(sensor, false);
        verify(securityRepository, times(1)).setAlarmStatus(AlarmStatus.NO_ALARM);
        verify(securityRepository, times(1)).updateSensor(sensor);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("If alarm is active, change in sensor state should not affect the alarm state")
    void ifAlarmIsActive_changeSensorShouldNotAffectAlarmState(boolean status) {
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.ALARM);
        securityService.changeSensorActivationStatus(sensor, status);
        verify(securityRepository, never()).setAlarmStatus(any(AlarmStatus.class));
        verify(securityRepository, times(1)).updateSensor(sensor);
    }

    @Test
    @DisplayName("If a sensor is activated while already active and the system is in pending state, change it to alarm state")
    void ifSensorAlreadyActivatedWhileActiveAndPendingAlarm_changeStatusToAlarm() {
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.PENDING_ALARM);
        sensor.setActive(true);
        securityService.changeSensorActivationStatus(sensor, true);
        verify(securityRepository, times(1)).setAlarmStatus(AlarmStatus.ALARM);
        verify(securityRepository, times(1)).updateSensor(sensor);
    }

    @ParameterizedTest
    @EnumSource(value = AlarmStatus.class, names = {"NO_ALARM", "PENDING_ALARM", "ALARM"})
    @DisplayName("If a sensor is deactivated while already inactive, make no changes to the alarm state.")
    void ifSensorIsDeactivatedWhileInactive_makeNoChangesToAlarmState(AlarmStatus status) {
        sensor.setActive(false);
        securityService.changeSensorActivationStatus(sensor, false);
        verify(securityRepository, never()).setAlarmStatus(any(AlarmStatus.class));
        verify(securityRepository, times(1)).updateSensor(sensor);
    }

    @Test
    @DisplayName("If the image service identifies an image containing a cat while the system is armed-home, " +
            "put the system into alarm status")
    void ifImageServiceIdentifiesImageContainACatWhileAlarmIsArmedHome_changeStatusToAlarm() {
        BufferedImage catImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
        when(securityRepository.getArmingStatus()).thenReturn(ArmingStatus.ARMED_HOME);
        when(fakeImageService.imageContainsCat(any(BufferedImage.class), ArgumentMatchers.anyFloat())).thenReturn(true);
        securityService.processImage(catImage);
        verify(securityRepository, times(1)).setAlarmStatus(AlarmStatus.ALARM);
    }

    @Test
    @DisplayName("If the image service identifies an image that does not contain a cat, " +
            "change the status to no alarm as long as the sensors are not active")
    void ifImageServiceIdentifiesImageNotContainACat_changeStatusToNoAlarmAsLongSensorsAreNotActive() {
        when(fakeImageService.imageContainsCat(any(BufferedImage.class), ArgumentMatchers.anyFloat())).thenReturn(false);
        securityService.processImage(mock(BufferedImage.class));
        verify(securityRepository, times(1)).setAlarmStatus(AlarmStatus.NO_ALARM);
    }

    @Test
    @DisplayName("If the system is disarmed, set the status to no alarm")
    void ifSystemIsDisarmed_setStatusToNoAlarmState() {
        securityService.setArmingStatus(ArmingStatus.DISARMED);
        verify(securityRepository, times(1)).setAlarmStatus(AlarmStatus.NO_ALARM);
    }

    @ParameterizedTest
    @EnumSource(value = ArmingStatus.class, names = {"ARMED_HOME", "ARMED_AWAY"})
    @DisplayName("If the system is armed, reset all sensors to inactive")
    void ifSystemIsArmed_resetAllSensorsToInactive(ArmingStatus status) {
        Set<Sensor> sensors = getAllSensors(5);
        when(securityRepository.getSensors()).thenReturn(sensors);
        securityService.setArmingStatus(status);
        securityService.getSensors().forEach(sensor -> {
            assertEquals(false, sensor.getActive());
        });
    }

    @Test
    @DisplayName("If the system is armed-home while the camera shows a cat, set the alarm status to alarm")
    void ifSystemIsArmedHomeWhileTheCameraShowsACat_setStatusToAlarm() {
        BufferedImage catImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
        when(fakeImageService.imageContainsCat(any(BufferedImage.class),anyFloat())).thenReturn(true);
        when(securityRepository.getArmingStatus()).thenReturn(ArmingStatus.ARMED_HOME);
        securityService.processImage(catImage);
        securityService.setArmingStatus(ArmingStatus.ARMED_HOME);
        verify(securityRepository, times(1)).setAlarmStatus(AlarmStatus.ALARM);
    }

    @Test
    @DisplayName("Test Add and Remove sensor")
    void testAddAndRemoveSensor() {
        securityService.addSensor(sensor);
        securityService.removeSensor(sensor);
        verify(securityRepository, times(1)).addSensor(sensor);
        verify(securityRepository, times(1)).removeSensor(sensor);
    }

    @Test
    @DisplayName("Test Add and Remove Status listener")
    void testAddAndRemoveStatusListener() {
        securityService.addStatusListener(statusListener);
        securityService.removeStatusListener(statusListener);
    }
}
