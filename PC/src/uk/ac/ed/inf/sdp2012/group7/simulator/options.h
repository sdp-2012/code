#ifndef __options_h__
#define __options_h__

#define DEFAULT_BLUE_PORT "10000"
#define DEFAULT_YELLOW_PORT "10001"
#define DEFAULT_WORLD_STATE_PORT "10002"

#define PITCH_WIDTH 640
#define PITCH_HEIGHT 480
#define PITCH_X1 40
#define PITCH_Y1 104
#define PITCH_X2 607
#define PITCH_Y2 384

#define BLUE_START_X 180
#define BLUE_START_Y 240
#define BLUE_START_A 52       // In degrees, NOT negative (so -90 = 270) kthxbai
#define YELLOW_START_X 440
#define YELLOW_START_Y 240
#define YELLOW_START_A 180   // Same as above

#define ROBOT_SIZE 46

#define USEC_PER_ACTION 40000 // 25 fps = action every 0.04 s

#define SPEED(s) (s/4)  // Work out a better formula

#define IPV4
#undef IPV6

#endif
