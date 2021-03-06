#ifndef __types_h__
#define __types_h__

#include <inttypes.h>

#include "opcodes.h"

#define opcode_t int32_t
#define socket_t int
#define java_int int32_t

struct command {
	char kicker;
	uint16_t arg;
	char instr;
};

struct robot_status {
	java_int x;
	java_int y;
	double angle;
	java_int kicker;
};

struct robot_thread_args {
	socket_t socket;
	int is_dummy;
	int robot;
	struct robot_status * status;
};

struct robot_action_thread_args {
	struct command * cmd;
	struct robot_status * rs;
	int robot;
	socket_t socket;
};

struct world_state {
	struct robot_status * blue;
	struct robot_status * yellow;
};

struct ws_thread_args {
	struct world_state * ws;
	socket_t socket;
	int is_dummy;
};

struct ws_packet {
	java_int blue_x;
	java_int blue_y;
	java_int blue_a;

	java_int yellow_x;
	java_int yellow_y;
	java_int yellow_a;

	java_int blue_kick;
	java_int yellow_kick;
};

#endif
