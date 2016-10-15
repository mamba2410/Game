package callummcgregor.common.entity;

import java.awt.Color;

import callummcgregor.common.terrain.World.Biome;

public class EnemyHellBlob extends EnemyBlobs {

	public EnemyHellBlob(byte blobJoins) {
		super(blobJoins);
		setEntityColor(new Color(0.5f, 0.5f, 0.1f));
		addSpawnableBiomes(Biome.HELL);
		removeSpawnableBiomes(Biome.EARTH);
	}

}
