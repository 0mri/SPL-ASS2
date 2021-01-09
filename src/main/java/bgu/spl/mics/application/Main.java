package bgu.spl.mics.application;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import bgu.spl.mics.application.services.LandoMicroservice;
import bgu.spl.mics.application.services.LeiaMicroservice;
import bgu.spl.mics.application.services.R2D2Microservice;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system. In
 * the end, you should output a JSON.
 */
public class Main {
	public final static long StartTime = System.currentTimeMillis();

	public static void main(String[] args) throws IOException {
		Input i;
		Gson g = new Gson();
		try {
			i = g.fromJson(new FileReader(args[0]), Input.class);
		} catch (IOException e) {
			throw e;
		}
		Ewoks.init(i.getEwoks());
		Diary.init();

		List<MicroService> micro_services = List.of(new LeiaMicroservice(i.getAttacks()),
				new R2D2Microservice(i.getR2D2()), new C3POMicroservice(), new LandoMicroservice(i.getLando()),
				new HanSoloMicroservice());

		ExecutorService execs = Executors.newFixedThreadPool(5);
		for (MicroService mcs : micro_services)
			execs.submit(mcs);

		execs.shutdown();
		while (!execs.isTerminated());

		System.out.println(Diary.getInstance());
		toJson(args[1]);
	}

	public static void toJson(String path) throws IOException {
		Gson gson = new Gson();
		try (FileWriter writer = new FileWriter(path)) {
			gson.toJson(Diary.getInstance(), writer);
		} catch (IOException e) {
			throw e;
		}
	}
}
