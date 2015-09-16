import java.util.*;
import lejos.hardware.motor.*;
import lejos.hardware.lcd.*;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
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

public class Main{
	public static void main (String[] arg) throws Exception  {
		
		// Definerer sensorer:
		Brick brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1"); // EV3-uttrasonicsensor
		Port s2 = brick.getPort("S2"); // EV3-trykksensor
		Port s3 = brick.getPort("S3"); // EV3-trykksensor

		EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(s1); // EV3-uttrasonicsensor
		EV3TouchSensor trykksensor1 = new EV3TouchSensor(s2); // EV3-trykksensor
		EV3TouchSensor trykksensor2 = new EV3TouchSensor(s3); // EV3-trykksensor
		
		/* Definerer en ultrasonicsensor */
		SampleProvider ultrasonicLeser = ultrasonicSensor.getDistanceMode();
		float[] ultrasonicSample = new float[ultrasonicLeser.sampleSize()]; // tabell som inneholder avlest verdi
		
		/* Definerer en trykksensor */
		SampleProvider trykkLeser1 = trykksensor1; // 1 eller 0
		float[] trykkSample1 = new float[trykkLeser1.sampleSize()]; // tabell som inneholder avlest verdi
		
		/* Definerer en trykksensor */
		SampleProvider trykkLeser2 = trykksensor2; // 1 eller 0
		float[] trykkSample2 = new float[trykkLeser2.sampleSize()]; // tabell som inneholder avlest verdi
		
		// Registrerer differentialPilot
		DifferentialPilot pilot = new DifferentialPilot(56, 120, Motor.B, Motor.C, false);
		pilot.setTravelSpeed(80);
		pilot.setRotateSpeed(150);
		
		// Kjør roboten
		boolean kjor = true;
		Random random = new Random();
		while (kjor) {
			// Unngå hindringer logikk
			int dir = random.nextInt(3);
			ultrasonicLeser.fetchSample(ultrasonicSample, 0);
			trykksensor1.fetchSample(trykkSample1, 0);
			trykksensor2.fetchSample(trykkSample2, 0);
			System.out.println(ultrasonicSample[0]);
			if (trykkSample1[0] > 0) {
				System.out.println("Trykket!");
				pilot.travel(-50);
				pilot.rotateRight();
			} else if (trykkSample2[0] > 0) {
				System.out.println("Trykket!");
				pilot.travel(-50);
				pilot.rotateLeft();
			} else if(ultrasonicSample[0] < 0.15) {
				if (dir == 0) {
					pilot.rotateLeft();
				} else {
					pilot.rotateRight();
				}
			} else{
				pilot.forward();
			}
			Thread.sleep(200);
				
		} //Avslutt while
	}
}