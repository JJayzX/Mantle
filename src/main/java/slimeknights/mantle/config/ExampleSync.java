package slimeknights.mantle.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

import slimeknights.mantle.network.NetworkWrapper;

class ExampleSync
{

  static NetworkWrapper networkWrapper;

  static ExampleSync INSTANCE;

  static void setup()
  {
    networkWrapper = new NetworkWrapper("mantle:example");
    networkWrapper.registerPacket(ExampleSyncPacketImpl.class, ExampleSyncPacketImpl::encode, ExampleSyncPacketImpl::decode, ExampleSyncPacketImpl.Handler::handle);
    INSTANCE = new ExampleSync();
  }

  @OnlyIn(Dist.CLIENT)
  private static boolean needsRestart;

  @SubscribeEvent
  @OnlyIn(Dist.DEDICATED_SERVER)
  public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
  {
    if (event.getPlayer() instanceof ServerPlayerEntity && FMLEnvironment.dist.isDedicatedServer())
    {
      ExampleSyncPacketImpl packet = new ExampleSyncPacketImpl();
      networkWrapper.sendTo(packet, (ServerPlayerEntity) event.getPlayer());
    }
  }

  @SubscribeEvent
  @OnlyIn(Dist.CLIENT)
  public void playerJoinedWorld(TickEvent.ClientTickEvent event)
  {
    ClientPlayerEntity player = Minecraft.getInstance().player;
    if (needsRestart)
    {
      player.sendMessage(new StringTextComponent("Configs synced with server. Configs require a restart"));
    }
    else
    {
      player.sendMessage(new StringTextComponent("Configs synced with server."));
    }
    MinecraftForge.EVENT_BUS.unregister(this);
  }

  static class ExampleConfig extends AbstractConfig
  {
    static ExampleConfig INSTANCE = new ExampleConfig();

    ExampleConfigFile exampleConfigFile;

    // call from preinit or something
    public void onPreInit(final FMLCommonSetupEvent event)
    {
      exampleConfigFile = this.load(new ExampleConfigFile(FMLPaths.CONFIGDIR.get().toFile()), ExampleConfigFile.class);

      // register this serverside to sync
      if (FMLEnvironment.dist.isDedicatedServer())
      {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
      }
    }
  }

  static class ExampleConfigFile extends AbstractConfigFile
  {

    public ExampleConfigFile(File configFolder)
    {
      super(configFolder, "exampleconfigfile");
    }

    @Override
    public void insertDefaults()
    {
      // no default values that need to initialized dynamically
    }

    @Override
    protected int getConfigVersion()
    {
      return 1;
    }
  }

  static class ExampleSyncPacketImpl extends AbstractConfigSyncPacket
  {

    protected static AbstractConfig getConfig()
    {
      return ExampleConfig.INSTANCE;
    }

    @Override
    protected boolean sync()
    {
      if (super.sync())
      {
        // clientside register only
        MinecraftForge.EVENT_BUS.register(INSTANCE);
        return true;
      }
      return false;
    }
  }
}
