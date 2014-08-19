LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := andenginephysicsbox2dextension
LOCAL_SRC_FILES := \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Android.mk \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Application.mk \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\build.bat \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\build.sh \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Android.mk \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Body.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\CircleShape.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Contact.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\ContactImpulse.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\DistanceJoint.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Fixture.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\FrictionJoint.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\GearJoint.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Joint.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\LineJoint.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Manifold.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\MouseJoint.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\PolygonShape.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\PrismaticJoint.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\PulleyJoint.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\RevoluteJoint.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Shape.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\World.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Collision\b2BroadPhase.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Collision\b2CollideCircle.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Collision\b2CollidePolygon.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Collision\b2Collision.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Collision\b2Distance.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Collision\b2DynamicTree.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Collision\b2TimeOfImpact.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Collision\Shapes\b2CircleShape.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Collision\Shapes\b2PolygonShape.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Common\b2BlockAllocator.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Common\b2Math.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Common\b2Settings.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Common\b2StackAllocator.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\b2Body.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\b2ContactManager.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\b2Fixture.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\b2Island.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\b2World.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\b2WorldCallbacks.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\Contacts\b2CircleContact.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\Contacts\b2Contact.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\Contacts\b2ContactSolver.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\Contacts\b2PolygonAndCircleContact.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\Contacts\b2PolygonContact.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\Contacts\b2TOISolver.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\Joints\b2DistanceJoint.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\Joints\b2FrictionJoint.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\Joints\b2GearJoint.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\Joints\b2Joint.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\Joints\b2LineJoint.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\Joints\b2MouseJoint.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\Joints\b2PrismaticJoint.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\Joints\b2PulleyJoint.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\Joints\b2RevoluteJoint.cpp \
	G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni\Box2D\Dynamics\Joints\b2WeldJoint.cpp \

LOCAL_C_INCLUDES += G:\android-program\Game\oGEngineDemo_flappyBird\src\main\jni
LOCAL_C_INCLUDES += G:\android-program\Game\oGEngineDemo_flappyBird\src\debug\jni

include $(BUILD_SHARED_LIBRARY)
