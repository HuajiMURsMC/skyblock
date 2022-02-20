package com.jsorrell.skyblock;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.settings.SettingsManager;
import com.jsorrell.skyblock.criterion.Criteria;
import com.jsorrell.skyblock.gen.SkyBlockChunkGenerator;
import com.jsorrell.skyblock.gen.SkyBlockGenerationSettings;
import com.jsorrell.skyblock.mixin.SpawnRestrictionAccessor;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.PiglinBruteEntity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;

public class SkyBlockExtension implements CarpetExtension, ModInitializer {
  private static SettingsManager settingsManager;

  public SkyBlockExtension() {
    CarpetServer.manageExtension(this);
  }

  @Override
  public void onInitialize() {
    settingsManager = new SettingsManager(Build.VERSION, Build.ID, Build.NAME);
    settingsManager.parseSettingsClass(SkyBlockSettings.class);
    // Restrict Piglin Brute spawning to the ground
    SpawnRestrictionAccessor.register(EntityType.PIGLIN_BRUTE, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PiglinBruteEntity::canSpawnInDark);
    Registry.register(
        Registry.CHUNK_GENERATOR, SkyBlockGenerationSettings.NAME, SkyBlockChunkGenerator.CODEC);
    Criteria.registerAll();

  }

  @Override
  public SettingsManager customSettingsManager() {
    return settingsManager;
  }

  @Override
  public String version() {
    return Build.ID + " " + Build.VERSION;
  }
}
