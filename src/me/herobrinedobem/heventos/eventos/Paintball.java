package me.herobrinedobem.heventos.eventos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBaseAPI;
import me.herobrinedobem.heventos.api.EventoCancellType;
import me.herobrinedobem.heventos.api.EventoUtils;
import me.herobrinedobem.heventos.api.events.StopEvent;
import me.herobrinedobem.heventos.api.events.TeamWinEvent;
import me.herobrinedobem.heventos.eventos.listeners.PaintballListener;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;

public class Paintball extends EventoBaseAPI {

	private PaintballListener listener;
	private List<String> timeAzul = new ArrayList<String>();
	private List<String> timeVermelho = new ArrayList<String>();

	public Paintball(YamlConfiguration config) {
		super(config);
		listener = new PaintballListener();
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(listener, HEventos.getHEventos());
	}

	@Override
	public void startEventMethod() {
		Collections.shuffle(getParticipantes());
		for (int i = 0; i < getParticipantes().size(); ++i) {
			String b = getParticipantes().get(i);
			if (i % 2 == 0) {
				getTimeAzul().add(b);
			} else {
				getTimeVermelho().add(b);
			}
		}
		for (String s : getParticipantes()) {
			darKit(getPlayerByName(s));
			Location Locvermelho = EventoUtils.getLocation(getConfig(), "Localizacoes.Pos_1");
			Location Locazul = EventoUtils.getLocation(getConfig(), "Localizacoes.Pos_2");
			Locazul.setY(Locazul.getY() + 1);
			Locvermelho.setY(Locvermelho.getY() + 1);
			if (timeAzul.contains(s)) {
				getPlayerByName(s).teleport(Locazul);
			} else if (timeVermelho.contains(s)) {
				getPlayerByName(s).teleport(Locvermelho);
			}
			getPlayerByName(s).setHealth(20.0);
		}
	}

	@Override
	public void scheduledMethod() {
		if ((isOcorrendo() == true) && (isAberto() == false)) {
			if (this.timeVermelho.isEmpty()) {
				String time = "Azul";
				TeamWinEvent event1 = new TeamWinEvent(time, this, getTimeAzul());
				HEventos.getHEventos().getServer().getPluginManager().callEvent(event1);
				getTimeAzul().clear();
				getTimeVermelho().clear();
				stopEvent();
			} else if (this.timeAzul.isEmpty()) {
				String time = "Vermelho";
				TeamWinEvent event1 = new TeamWinEvent(time, this, getTimeVermelho());
				HEventos.getHEventos().getServer().getPluginManager().callEvent(event1);
				getTimeAzul().clear();
				getTimeVermelho().clear();
				stopEvent();
			}
		}
	}

	@Override
	public void cancelEventMethod() {
		sendMessageList("Mensagens.Cancelado");
	}

	@Override
	public void stopEvent() {
		StopEvent event = new StopEvent(HEventos.getHEventos().getEventosController().getEvento(),
				EventoCancellType.FINISHED);
		HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
	}
	
	@Override
	public void resetEvent() {
		super.resetEvent();
		BukkitEventHelper.unregisterEvents(listener, HEventos.getHEventos());
	}

	private void darKit(Player player) {
		ItemStack arco = new ItemStack(Material.BOW, 1);
		arco.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		if (getTimeAzul().contains(player.getName())) {
			player.getInventory().addItem(arco);
			player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
			ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
			LeatherArmorMeta lam = (LeatherArmorMeta) lhelmet.getItemMeta();
			lam.setColor(Color.BLUE);
			lhelmet.setItemMeta(lam);
			ItemStack lChest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			LeatherArmorMeta lcm = (LeatherArmorMeta) lChest.getItemMeta();
			lcm.setColor(Color.BLUE);
			lChest.setItemMeta(lcm);
			ItemStack lLegg = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			LeatherArmorMeta llg = (LeatherArmorMeta) lLegg.getItemMeta();
			llg.setColor(Color.BLUE);
			lLegg.setItemMeta(llg);
			ItemStack lBoots = new ItemStack(Material.LEATHER_BOOTS, 1);
			LeatherArmorMeta lbo = (LeatherArmorMeta) lBoots.getItemMeta();
			lbo.setColor(Color.BLUE);
			lBoots.setItemMeta(lbo);
			player.getInventory().setHelmet(lhelmet);
			player.getInventory().setChestplate(lChest);
			player.getInventory().setLeggings(lLegg);
			player.getInventory().setBoots(lBoots);
			player.updateInventory();
		} else {
			player.getInventory().addItem(arco);
			player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
			ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
			LeatherArmorMeta lam = (LeatherArmorMeta) lhelmet.getItemMeta();
			lam.setColor(Color.RED);
			lhelmet.setItemMeta(lam);
			ItemStack lChest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			LeatherArmorMeta lcm = (LeatherArmorMeta) lChest.getItemMeta();
			lcm.setColor(Color.RED);
			lChest.setItemMeta(lcm);
			ItemStack lLegg = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			LeatherArmorMeta llg = (LeatherArmorMeta) lLegg.getItemMeta();
			llg.setColor(Color.RED);
			lLegg.setItemMeta(llg);
			ItemStack lBoots = new ItemStack(Material.LEATHER_BOOTS, 1);
			LeatherArmorMeta lbo = (LeatherArmorMeta) lBoots.getItemMeta();
			lbo.setColor(Color.RED);
			lBoots.setItemMeta(lbo);
			player.getInventory().setHelmet(lhelmet);
			player.getInventory().setChestplate(lChest);
			player.getInventory().setLeggings(lLegg);
			player.getInventory().setBoots(lBoots);
			player.updateInventory();
		}
	}

	public List<String> getTimeAzul() {
		return this.timeAzul;
	}

	public List<String> getTimeVermelho() {
		return this.timeVermelho;
	}
}
