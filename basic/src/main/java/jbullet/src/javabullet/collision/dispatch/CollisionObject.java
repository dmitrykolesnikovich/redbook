/*
 * Java port of Bullet (c) 2008 Martin Dvorak <jezek2@advel.cz>
 *
 * Bullet Continuous Collision Detection and Physics Library
 * Copyright (c) 2003-2007 Erwin Coumans  http://continuousphysics.com/Bullet/
 *
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from
 * the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose, 
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package javabullet.collision.dispatch;

import javabullet.BulletStack;
import javabullet.collision.broadphase.BroadphaseProxy;
import javabullet.collision.shapes.CollisionShape;
import javabullet.linearmath.Transform;

import javax.vecmath.Vector3f;

/**
 * CollisionObject can be used to manage collision detection objects.
 * CollisionObject maintains all information that is needed for a collision detection: Shape, Transform and AABB proxy.
 * They can be added to the CollisionWorld.
 *
 * @author jezek2
 */
public class CollisionObject {

    // island management, m_activationState1
    public static final int ACTIVE_TAG = 1;
    public static final int ISLAND_SLEEPING = 2;
    public static final int WANTS_DEACTIVATION = 3;
    public static final int DISABLE_DEACTIVATION = 4;
    public static final int DISABLE_SIMULATION = 5;
    protected final BulletStack stack = BulletStack.get();
    ///m_interpolationWorldTransform is used for CCD and interpolation
    ///it can be either previous or future (predicted) transform
    protected final Transform interpolationWorldTransform = new Transform();
    //those two are experimental: just added for bullet time effect, so you can still apply impulses (directly modifying velocities)
    //without destroying the continuous interpolated motion (which uses this interpolation velocities)
    protected final Vector3f interpolationLinearVelocity = new Vector3f();
    protected final Vector3f interpolationAngularVelocity = new Vector3f();
    protected Transform worldTransform = new Transform();
    protected BroadphaseProxy broadphaseHandle;
    protected CollisionShape collisionShape;
    protected int collisionFlags;
    protected int islandTag1;
    protected int companionId;
    protected int activationState1;
    protected float deactivationTime;
    protected float friction;
    protected float restitution;

    ///users can point to their objects, m_userPointer is not used by Bullet, see setUserPointer/getUserPointer
    protected Object userObjectPointer;

    ///m_internalOwner is reserved to point to Bullet's btRigidBody. Don't use this, use m_userObjectPointer instead.
    protected Object internalOwner;

    ///time of impact calculation
    protected float hitFraction;
    ///Swept sphere radius (0.0 by default), see btConvexConvexAlgorithm::
    protected float ccdSweptSphereRadius;

    /// Don't do continuous collision detection if square motion (in one step) is less then m_ccdSquareMotionThreshold
    protected float ccdSquareMotionThreshold;
    /// If some object should have elaborate collision filtering by sub-classes
    protected boolean checkCollideWith;

    public CollisionObject() {
        this.collisionFlags = CollisionFlags.STATIC_OBJECT;
        this.islandTag1 = -1;
        this.companionId = -1;
        this.activationState1 = 1;
        this.friction = 0.5f;
        this.hitFraction = 1f;
    }

    public boolean checkCollideWithOverride(CollisionObject co) {
        return true;
    }

    public boolean mergesSimulationIslands() {
        ///static objects, kinematic and object without contact response don't merge islands
        return ((collisionFlags & (CollisionFlags.STATIC_OBJECT | CollisionFlags.KINEMATIC_OBJECT | CollisionFlags.NO_CONTACT_RESPONSE)) == 0);
    }

    public boolean isStaticObject() {
        return (collisionFlags & CollisionFlags.STATIC_OBJECT) != 0;
    }

    public boolean isKinematicObject() {
        return (collisionFlags & CollisionFlags.KINEMATIC_OBJECT) != 0;
    }

    public boolean isStaticOrKinematicObject() {
        return (collisionFlags & (CollisionFlags.KINEMATIC_OBJECT | CollisionFlags.STATIC_OBJECT)) != 0;
    }

    public boolean hasContactResponse() {
        return (collisionFlags & CollisionFlags.NO_CONTACT_RESPONSE) == 0;
    }

    public CollisionShape getCollisionShape() {
        return collisionShape;
    }

    public void setCollisionShape(CollisionShape collisionShape) {
        this.collisionShape = collisionShape;
    }

    public int getActivationState() {
        return activationState1;
    }

    public void setActivationState(int newState) {
        if ((activationState1 != DISABLE_DEACTIVATION) && (activationState1 != DISABLE_SIMULATION)) {
            this.activationState1 = newState;
        }
    }

    public float getDeactivationTime() {
        return deactivationTime;
    }

    public void setDeactivationTime(float deactivationTime) {
        this.deactivationTime = deactivationTime;
    }

    public void forceActivationState(int newState) {
        this.activationState1 = newState;
    }

    public void activate() {
        activate(false);
    }

    public void activate(boolean forceActivation) {
        if (forceActivation || (collisionFlags & (CollisionFlags.STATIC_OBJECT | CollisionFlags.KINEMATIC_OBJECT)) == 0) {
            setActivationState(ACTIVE_TAG);
            deactivationTime = 0f;
        }
    }

    public boolean isActive() {
        return ((getActivationState() != ISLAND_SLEEPING) && (getActivationState() != DISABLE_SIMULATION));
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    // reserved for Bullet internal usage
    public Object getInternalOwner() {
        return internalOwner;
    }

    public Transform getWorldTransform() {
        return worldTransform;
    }

    public void setWorldTransform(Transform worldTransform) {
        this.worldTransform.set(worldTransform);
    }

    public BroadphaseProxy getBroadphaseHandle() {
        return broadphaseHandle;
    }

    public void setBroadphaseHandle(BroadphaseProxy broadphaseHandle) {
        this.broadphaseHandle = broadphaseHandle;
    }

    public Transform getInterpolationWorldTransform() {
        return interpolationWorldTransform;
    }

    public void setInterpolationWorldTransform(Transform interpolationWorldTransform) {
        this.interpolationWorldTransform.set(interpolationWorldTransform);
    }

    public Vector3f getInterpolationLinearVelocity() {
        return interpolationLinearVelocity;
    }

    public Vector3f getInterpolationAngularVelocity() {
        return interpolationAngularVelocity;
    }

    public int getIslandTag() {
        return islandTag1;
    }

    public void setIslandTag(int islandTag) {
        this.islandTag1 = islandTag;
    }

    public int getCompanionId() {
        return companionId;
    }

    public void setCompanionId(int companionId) {
        this.companionId = companionId;
    }

    public float getHitFraction() {
        return hitFraction;
    }

    public void setHitFraction(float hitFraction) {
        this.hitFraction = hitFraction;
    }

    public int getCollisionFlags() {
        return collisionFlags;
    }

    public void setCollisionFlags(int collisionFlags) {
        this.collisionFlags = collisionFlags;
    }

    // Swept sphere radius (0.0 by default), see btConvexConvexAlgorithm::
    public float getCcdSweptSphereRadius() {
        return ccdSweptSphereRadius;
    }

    // Swept sphere radius (0.0 by default), see btConvexConvexAlgorithm::
    public void setCcdSweptSphereRadius(float ccdSweptSphereRadius) {
        this.ccdSweptSphereRadius = ccdSweptSphereRadius;
    }

    public float getCcdSquareMotionThreshold() {
        return ccdSquareMotionThreshold;
    }

    // Don't do continuous collision detection if square motion (in one step) is less then m_ccdSquareMotionThreshold
    public void setCcdSquareMotionThreshold(float ccdSquareMotionThreshold) {
        this.ccdSquareMotionThreshold = ccdSquareMotionThreshold;
    }

    public Object getUserPointer() {
        return userObjectPointer;
    }

    public void setUserPointer(Object userObjectPointer) {
        this.userObjectPointer = userObjectPointer;
    }

    public boolean checkCollideWith(CollisionObject co) {
        if (checkCollideWith) {
            return checkCollideWithOverride(co);
        }

        return true;
    }
}
