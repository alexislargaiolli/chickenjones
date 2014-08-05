package fr.alex.games;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import fr.alex.games.entity.Arrow;
import fr.alex.games.entity.Ignore;
import fr.alex.games.entity.Mortal;
import fr.alex.games.entity.Player;
import fr.alex.games.entity.UserData;

public class GameCollisions implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Object o1 = contact.getFixtureA().getUserData();
		Object o2 = contact.getFixtureB().getUserData();
		Body b1 = contact.getFixtureA().getBody();
		Body b2 = contact.getFixtureB().getBody();
		if (o1 != null && o1 instanceof Arrow) {
			arrowContact((Arrow) o1, b1, o2, b2, contact);
		}
		if (o2 != null && o2 instanceof Arrow) {
			arrowContact((Arrow) o2, b2, o1, b1, contact);
		}
		if (o1 != null && o1 instanceof Player) {
			playerContact((Player) o1, b1, o2, b2, contact);
		}
		if (o2 != null && o2 instanceof Player) {
			playerContact((Player) o2, b2, o1, b1, contact);
		}
	}

	private void arrowContact(Arrow arrow, Body arrowBody, Object other, Body otherBody, Contact contact) {
		if (other instanceof UserData) {
			if (other instanceof Ignore) {
				contact.setEnabled(false);
			} else {
				arrowContactUserData(arrow, arrowBody, (UserData) other, otherBody);
			}
		}
		if (other instanceof Player) {
			arrow.setDead(true);
		}
		arrow.setTimeBeforeDeath(3);
	}

	private void playerContact(Player player, Body playerBody, Object other, Body otherBody, Contact contact) {

		player.setJumping(false);
		if (other instanceof UserData) {
			if (other instanceof Ignore) {
				contact.setEnabled(false);
			} else {
				((UserData) other).playerContact(otherBody, player);
			}
		} else if (other instanceof Arrow) {
			((Arrow) other).setDead(true);
		} else {
			player.setJumping(false);
		}
	}

	private void arrowContactArrow(Arrow arrow1, Arrow arrow2) {

	}

	private void arrowContactUserData(Arrow arrow1, Body arrowBody, UserData ud, Body udBody) {
		ud.hit(udBody);
		if (ud instanceof Mortal) {
			arrow1.setTimeBeforeDeath(0);
			arrow1.setDead(true);
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
