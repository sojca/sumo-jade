package crossRoadsNextGen.behaviors;

public enum J3Constraints {
    NORTH_SOUTH_GREEN {
        public String toString() {
            return "rrrrGGgrrrrGGg";
        }
    },
    NORTH_SOUTH_YELLOW {
        public String toString() {
            return "rrrryygrrrryyg";
        }
    },
    NORTH_SOUTH_CROSS_GREEN {
        public String toString() {
            return "rrrrrrGrrrrrrG";
        }
    },
    NORTH_SOUTH_CROSS_YELLOW {
        public String toString() {
            return "rrrrrryrrrrrry";
        }
    },
    WEST_EAST_GREEN {
        public String toString() {
            return "GGGgrrrGGGgrrr";
        }
    },
    WEST_EAST_YELLOW {
        public String toString() {
            return "yyygrrryyygrrr";
        }
    },
    WEST_EAST_CROSS_GREEN {
        public String toString() {
            return "rrrGrrrrrrGrrr";
        }
    },
    WEST_EAST_CROSS_YELLOW {
        public String toString() {
            return "rrryrrrrrryrrr";
        }
    }
}
