
# Breakout

Breakout is a Java-based 2D game engine and demo featuring continuous collision detection between circles and line segments. It includes a simple bouncing ball physics engine and can be used as a library for building classic games like Breakout.

## Features

- Continuous collision detection (circle vs. line segment)
- Simple 2D physics engine
- Debugger mode for visualizing collisions and trajectories
- Ready-to-use Breakout game demo

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven

### Build & Run

Clone the repository and use Maven to build and run:

```sh
mvn clean install
```

#### Run Debugger

```sh
mvn javafx:run -Pdebugger
```

#### Run Breakout

```sh
mvn javafx:run -Pbreakout
```

## Demo

**Debugger**
[Debugger](https://github.com/user-attachments/assets/62305376-e13d-450e-813e-401cba68d1da)

**Breakout**
[Breakout](https://github.com/user-attachments/assets/a72b0965-f39b-416f-b196-5c04f24f3f87)

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
