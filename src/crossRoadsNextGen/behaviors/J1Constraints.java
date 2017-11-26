package crossRoadsNextGen.behaviors;

public enum J1Constraints {
    NORTH_SOUTH_GREEN {
        public String toString() {
            return "rrGGrrrrGg";
        }
    },
    NORTH_SOUTH_YELLOW {
        public String toString() {
            return "rryyrrrryg";
        }
    },
    NORTH_SOUTH_CROSS_GREEN {
        public String toString() {
            return "rrrrrrrrrG";
        }
    },
    NORTH_SOUTH_CROSS_YELLOW {
        public String toString() {
            return "rrrrrrrrry";
        }
    },
    WEST_EAST_GREEN {
        public String toString() {
            return "GgrrGGGgrr";
        }
    },
    WEST_EAST_YELLOW {
        public String toString() {
            return "ygrryyygrr";
        }
    },
    WEST_EAST_CROSS_GREEN {
        public String toString() {
            return "rGrrrrrGrr";
        }
    },
    WEST_EAST_CROSS_YELLOW {
        public String toString() {
            return "ryrrrrryrr";
        }
    }
}
