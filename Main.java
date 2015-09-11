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


public class Main{
	public static void main (String[] arg) throws Exception  {
		
		// Definerer sensorer:
		Brick brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1"); // EV3-uttrasonicsensor
		Port s2 = brick.getPort("S2"); // EV3-trykksensor
		EV3UltrasonicSensor ultraSensor = new EV3UltrasonicSensor(s1); // EV3-uttrasonicsensor
		EV3TouchSensor trykksensor = new EV3TouchSensor(s2); // EV3-trykksensor
		
		/* Definerer en ultrasonisksensor */
		SampleProvider ultraLeser = ultraSensor.getDistanceMode();
		float[] ultraSample = new float[ultraLeser.sampleSize()]; // tabell som inneholder avlest verdi
		
		/* Definerer en trykksensor */
		SampleProvider trykkLeser = trykksensor; // 1 eller 0
		float[] trykkSample = new float[trykkLeser.sampleSize()]; // tabell som inneholder avlest verdi
		
		// Registrerer differentialPilot
		DifferentialPilot  pilot = new DifferentialPilot (2.1f, 4.4f, Motor.A, Motor.C, true);
		pilot.setTravelSpeed(450);
		int distance = 100000;
		
		// Kjør roboten
		boolean kjor = true;
		while (kjor) {
			
			if(ultraSample[0]<1.2) {
				pilot.travel(distance);
			} else pilot.rotate (45);
			
			// Trykksensor ting
			trykksensor.fetchSample(trykkSample, 0);
			if (trykkSample[0] > 0){
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
				kjor = false;
				Motor.A.stop();
				Motor.C.stop();
				Motor.B.stop();
			}
		}
		
		System.out.println("Avsluttet");
		Thread.sleep(300);
		
	}
}