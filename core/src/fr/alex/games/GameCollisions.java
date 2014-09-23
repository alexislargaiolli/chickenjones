package fr.alex.games;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import fr.alex.games.entity.Arrow;
import fr.alex.games.entity.Chicken;
import fr.alex.games.entity.UserData;

public class GameCollisions implements ContactListener {

	public static List<StickyInfo> arrowToStick = new ArrayList<StickyInfo>();

	@Override
	public void beginContact(Contact contact) {
		
	}

	private void arrowContact(Arrow arrow, Body arrowBody, Object other, Body otherBody, Contact contact) {
		if (other instanceof UserData) {
			UserData ud = (UserData) other;
			arrowContactUserData(arrow, arrowBody, ud, otherBody);
			if (ud.isStick()) {
				Vector2 contactPoint = contact.getWorldManifold().getPoints()[0];
				
				if (contactPoint.dst(arrow.getHead()) < .05f) {
					arrow.getmBody().setLinearVelocity(0, 0);
					arrowToStick.add(new StickyInfo(arrow, ud));
				}
			}
		}
		if (other instanceof Chicken) {
			//arrow.setDead(true);
			contact.setEnabled(false);
		}

		arrow.setTimeBeforeDeath(3);
	}

	private void playerContact(Chicken player, Body playerBody, Object other, Body otherBody, Contact contact) {

		player.setJumping(false);
		if (other instanceof UserData) {
			((UserData) other).playerContact(otherBody, player);

		} else if (other instanceof Arrow) {
			//((Arrow) other).setDead(true);
			contact.setEnabled(false);			
		} else {
			player.setJumping(false);
		}
	}

	private void arrowContactArrow(Arrow arrow1, Arrow arrow2) {

	}

	private void arrowContactUserData(Arrow arrow1, Body arrowBody, UserData ud, Body udBody) {
		if (arrow1.maxContactReached()) {
			if (ud.isCoin()) {
				GM.hitCount++;
				ud.hit(udBody);
			}
		} else {
			if (ud.isDestroyable()) {
				GM.hitCount++;
			}
			ud.hit(udBody);
			arrow1.contact();
			if (ud.isMortal()) {
				arrow1.setTimeBeforeDeath(0);
				arrow1.setDead(true);
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Object o1 = contact.getFixtureA().getBody().getUserData();
		Object o2 = contact.getFixtureB().getBody().getUserData();
		Body b1 = contact.getFixtureA().getBody();
		Body b2 = contact.getFixtureB().getBody();
		if (o1 != null && o1 instanceof Arrow) {
			arrowContact((Arrow) o1, b1, o2, b2, contact);
		}
		if (o2 != null && o2 instanceof Arrow) {
			arrowContact((Arrow) o2, b2, o1, b1, contact);
		}
		if (o1 != null && o1 instanceof Chicken) {
			playerContact((Chicken) o1, b1, o2, b2, contact);
		}
		if (o2 != null && o2 instanceof Chicken) {
			playerContact((Chicken) o2, b2, o1, b1, contact);
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
