package chrislo27.kilojoule.core.universe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.stream.NbtInputStream;
import com.evilco.mc.nbt.stream.NbtOutputStream;
import com.evilco.mc.nbt.tag.ITag;
import com.evilco.mc.nbt.tag.TagCompound;
import com.evilco.mc.nbt.tag.TagInteger;
import com.evilco.mc.nbt.tag.TagList;
import com.evilco.mc.nbt.tag.TagLong;
import com.evilco.mc.nbt.tag.TagString;

import chrislo27.kilojoule.core.chunk.Chunk;
import chrislo27.kilojoule.core.entity.Entity;
import chrislo27.kilojoule.core.nbt.BlockIDMap;
import chrislo27.kilojoule.core.registry.reflection.Entities;
import chrislo27.kilojoule.core.registry.reflection.Worlds;
import chrislo27.kilojoule.core.world.World;
import ionium.templates.Main;

public class UniverseSavingLoading {

	private static BlockIDMap blockMap = null;

	public static void save(Universe universe, FileHandle directory) throws IOException {
		if (blockMap == null) blockMap = new BlockIDMap("BlockIDMap");

		directory.mkdirs();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		NbtOutputStream nbtStream = new NbtOutputStream(baos);
		FileOutputStream outputStream = new FileOutputStream(
				directory.file().getAbsolutePath() + "/universe.dat");
		GZIPOutputStream gzipStream = new GZIPOutputStream(outputStream);

		TagCompound root = new TagCompound("Universe");

		root.setTag(new TagLong("Seed", universe.seed));

		// worlds
		for (Entry<String, World> world : universe.worlds.entries()) {
			TagString worldTag = new TagString("World", world.key);
			root.setTag(worldTag);

			TagCompound tc = getTagForWorld(world.value, world.key);

			FileOutputStream worldfos = new FileOutputStream(
					directory.file().getAbsolutePath() + "/" + world.key + ".dat");
			GZIPOutputStream worldgzipStream = new GZIPOutputStream(worldfos);

			// save world file
			nbtStream.write(tc);
			worldgzipStream.write(baos.toByteArray());
			baos.reset();

			worldgzipStream.close();
			worldfos.close();
		}

		// save universe file
		baos.reset();
		nbtStream.write(root);
		gzipStream.write(baos.toByteArray());

		nbtStream.close();
		baos.close();
		gzipStream.close();
		outputStream.close();
		
		System.gc();
	}

	public static byte[] loadBytes(FileHandle file) throws IOException {
		FileInputStream fis = new FileInputStream(file.file());
		GZIPInputStream gzipstream = new GZIPInputStream(fis);

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(2048);
		byte[] buffer = new byte[2048];
		int bytesRead;
		while ((bytesRead = gzipstream.read(buffer)) > 0) {
			byteStream.write(buffer, 0, bytesRead);
		}

		gzipstream.close();
		fis.close();

		return byteStream.toByteArray();
	}

	public static Universe load(FileHandle directory) throws IOException {
		Universe universe = null;

		byte[] universeFile = loadBytes(
				new FileHandle(directory.file().getAbsolutePath() + "/universe.dat"));

		ByteArrayInputStream universeStream = new ByteArrayInputStream(universeFile);
		NbtInputStream universeNbtStream = new NbtInputStream(universeStream);

		TagCompound universeRoot = (TagCompound) universeNbtStream.readTag();
		Map<String, ITag> allTags = universeRoot.getTags();
		Array<String> worldList = new Array<>();

		for (ITag itag : allTags.values()) {
			if (itag instanceof TagString && itag.getName().equals("World")) {
				worldList.add(((TagString) itag).getValue());
			}
		}

		universe = new Universe(universeRoot.getLong("Seed"));

		for (String s : worldList) {
			byte[] worldBytes = loadBytes(
					new FileHandle(directory.file().getAbsolutePath() + "/" + s + ".dat"));
			NbtInputStream worldNbtStream = new NbtInputStream(
					new ByteArrayInputStream(worldBytes));

			TagCompound worldRoot = (TagCompound) worldNbtStream.readTag();

			World w = loadTagForWorld(universe, worldRoot);

			universe.worlds.put(s, w);

			worldNbtStream.close();
		}

		universeNbtStream.close();
		universeStream.close();

		universe.player = null;

		System.gc();
		
		return universe;
	}

	public static TagCompound getTagForWorld(World world, String worldName) {
		TagCompound root = new TagCompound("World-" + worldName);

		// block map
		root.setTag(blockMap.getTag());

		// world data
		{
			root.setTag(new TagInteger("WorldWidth", world.worldWidth));
			root.setTag(new TagInteger("WorldHeight", world.worldHeight));
			root.setTag(new TagString("ReflectionID", Worlds.getWorldKey(world.getClass())));
		}

		// chunks
		{
			TagCompound allChunks = new TagCompound("Chunks");

			for (int x = 0; x < world.chunksWidth; x++) {
				for (int y = 0; y < world.chunksHeight; y++) {
					Chunk c = world.getChunk(x, y);
					TagCompound chunkTag = new TagCompound(
							"Chunk [" + c.chunkX + ", " + c.chunkY + "]");

					c.writeToNBT(chunkTag, blockMap);

					allChunks.setTag(chunkTag);
				}
			}

			root.setTag(allChunks);
		}

		// entities
		{
			TagList entityList = new TagList("Entities");

			for (Entity e : world.getAllEntities()) {
				String entityId = Entities.getEntityID(e.getClass());

				if (entityId == null) {
					Main.logger.warn("Entity with class " + e.getClass().getSimpleName()
							+ " doesn't have a reflection key");

					continue;
				}

				TagCompound tc = new TagCompound(entityId);

				tc.setTag(new TagString("ReflectionID", entityId));
				e.writeToNBT(tc);

				entityList.addTag(tc);
			}

			root.setTag(entityList);
		}

		return root;
	}

	public static World loadTagForWorld(Universe universe, TagCompound root)
			throws UnexpectedTagTypeException, TagNotFoundException {
		if (blockMap == null) blockMap = new BlockIDMap("BlockIDMap");

		World world = null;

		blockMap.loadFromTag(root.getCompound("BlockIDMap"));

		// world data
		int width = root.getInteger("WorldWidth");
		int height = root.getInteger("WorldHeight");

		Class<? extends World> clazz = Worlds.getWorldClass(root.getString("ReflectionID"));

		try {
			world = clazz.getConstructor(Universe.class, int.class, int.class).newInstance(universe,
					width, height);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}

		// chunks
		{
			TagCompound chunksRoot = root.getCompound("Chunks");

			Map<String, ITag> allTags = chunksRoot.getTags();

			for (ITag itag : allTags.values()) {
				if (itag instanceof TagCompound) {
					TagCompound chunkTag = (TagCompound) itag;
					Chunk c = new Chunk(chunkTag.getInteger("PositionX"),
							chunkTag.getInteger("PositionY"));

					c.readFromNBT(chunkTag, blockMap);

					world.setChunk(c, c.chunkX, c.chunkY);
				}
			}
		}

		// entities
		{
			List<TagCompound> entityList = root.getList("Entities", TagCompound.class);

			for (TagCompound tc : entityList) {
				String entityID = tc.getString("ReflectionID");
				Class<? extends Entity> eClazz = Entities.getEntityClass(entityID);

				if (eClazz == null) {
					Main.logger.warn("Entity class (" + entityID + ") wasn't found");
					continue;
				}

				Entity e = Entities.getNewInstance(entityID, world, 0, 0);

				e.readFromNBT(tc);

				world.addEntity(e);
			}
		}

		return world;
	}

}
