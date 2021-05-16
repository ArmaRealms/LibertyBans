/*
 * LibertyBans
 * Copyright © 2021 Anand Beh
 *
 * LibertyBans is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * LibertyBans is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with LibertyBans. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU Affero General Public License.
 */
package space.arim.libertybans.it.env;

import jakarta.inject.Inject;
import space.arim.api.chat.SendableMessage;
import space.arim.api.env.PlatformHandle;
import space.arim.api.env.annote.PlatformPlayer;
import space.arim.libertybans.core.config.InternalFormatter;
import space.arim.libertybans.core.env.AbstractEnvEnforcer;
import space.arim.libertybans.core.env.TargetMatcher;
import space.arim.libertybans.it.env.platform.QuackPlatform;
import space.arim.libertybans.it.env.platform.QuackPlayer;

import java.net.InetAddress;
import java.util.UUID;
import java.util.function.Consumer;

public class QuackEnforcer extends AbstractEnvEnforcer {

	private final QuackPlatform platform;

	@Inject
	public QuackEnforcer(InternalFormatter formatter, PlatformHandle handle, QuackPlatform platform) {
		super(formatter, handle);
		this.platform = platform;
	}

	@Override
	protected void sendToThoseWithPermission0(String permission, SendableMessage message) {
		for (QuackPlayer player : platform.getAllPlayers()) {
			if (player.hasPermission(permission)) {
				player.sendMessage(message);
			}
		}
	}

	@Override
	public void doForPlayerIfOnline(UUID uuid, Consumer<@PlatformPlayer Object> callback) {
		QuackPlayer player = platform.getPlayer(uuid);
		if (player != null) {
			callback.accept(player);
		}
	}

	@Override
	public void enforceMatcher(TargetMatcher matcher) {
		for (QuackPlayer player : platform.getAllPlayers()) {
			if (matcher.matches(player.getUniqueId(), player.getAddress())) {
				matcher.callback().accept(player);
			}
		}
	}

	@Override
	public UUID getUniqueIdFor(@PlatformPlayer Object player) {
		return ((QuackPlayer) player).getUniqueId();
	}

	@Override
	public InetAddress getAddressFor(@PlatformPlayer Object player) {
		return ((QuackPlayer) player).getAddress();
	}

}