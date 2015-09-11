import lejos.hardware.motor.*;
import lejos.hardware.lcd.*;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.hardware.sensor.NXTColorSensor;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.hardware.port.Port;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.Keys;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.hardware.sensor.*;
import lejos.robotics.navigation.DifferentialPilot;
// Kanskje bruke dette
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.FeatureDetectorAdapter;
import lejos.robotics.objectdetection.RangeFeatureDetector;


public class Main{
	public static void main (String[] arg) throws Exception  {
		
		// Definerer sensorer:
		Brick brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1"); // EV3-uttrasonicsensor
		Port s3 = brick.getPort("S3"); // EV3-trykksensor
		Port s4 = brick.getPort("S4"); // EV3-trykksensor
		EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(s1); // EV3-uttrasonicsensor
		EV3TouchSensor trykksensor1 = new EV3TouchSensor(s3); // EV3-trykksensor
		EV3TouchSensor trykksensor2 = new EV3TouchSensor(s4); // EV3-trykksensor
		
		/* Definerer en trykksensor */
		SampleProvider ultrasonicLeser = ultrasonicSensor.getDistanceMode();
		float[] ultrasonicSample = new float[ultrasonicLeser.sampleSize()]; // tabell som inneholder avlest verdi
		
		/* Definerer en trykksensor */
		SampleProvider trykkLeser1 = trykksensor1; // 1 eller 0
		float[] trykkSample1 = new float[trykkLeser1.sampleSize()]; // tabell som inneholder avlest verdi
		
		SampleProvider trykkLeser2 = trykksensor2; // 1 eller 0
		float[] trykkSample2 = new float[trykkLeser2.sampleSize()]; // tabell som inneholder avlest verdi
		
		// Registrerer differentialPilot
		DifferentialPilot pilot = new DifferentialPilot(56, 120, Motor.B, Motor.C, false);
		pilot.setTravelSpeed(200);
		pilot.setRotateSpeed(100);
		
		// Kjør roboten
		boolean kjor = true;
		while (kjor) {
			
			// Unngå hindringer logikk
			trykksensor1.fetchSample(trykkSample1, 0);
			trykksensor2.fetchSample(trykkSample2, 0);
			ultrasonicLeser.fetchSample(ultrasonicSample, 0);
			if(trykkSample1[0] > 0) {
				pilot.travel(-50);
				pilot.rotate(-60);
			} else if (trykkSample2[0] > 0) {
				pilot.travel(-50);
				pilot.rotate(60);
			} else {
				if(ultrasonicSample[0] < 0.2) {
					pilot.rotateRight();
				} else {
					pilot.forward();
				}
			}
			
			/*
			if (Button.ENTER.isPressed()){
				System.out.println("Avslutter");
				Thread.sleep(100);
				LCD.clear();
				System.out.println("Avslutter.");
				Thread.sleep(100);
				LCD.clear();
				System.out.println("Avslutter..");
				Thread.sleep(100);
				LCD.clear();
				System.out.println("Avslutter...");
				Thread.sleep(100);
				LCD.clear();
				pilot.stop();
				kjor = false;
			}
			*/
		}
		
		System.out.println("Avsluttet");
		Thread.sleep(300);
		
	}
}