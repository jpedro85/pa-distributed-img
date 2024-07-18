# pa-distributed-img

## Project Overview

The `pa-distributed-img` project is designed to demonstrate advanced programming skills by implementing a distributed image processing system in Java. This system allows users to convert images to grayscale using multiple servers, optimizing resource usage and ensuring efficient processing. The project focuses on parallel computing concepts, including threads, semaphores, locks, and architectural patterns for parallel development.

## Features

- **Parallel Image Processing:** Images are divided into equal parts and processed in parallel across multiple servers.
- **Load Balancing:** Workload is distributed based on the current load of each server to ensure balanced processing.
- **Server Management:** Users can increase the number of servers and view their status through an intuitive interface.
- **Output Log:** A log window displays the status and results of the image processing.
- **Image Tabs:** Users can manage multiple images to be processed through tabbed navigation.
- **User Interface:** A simple interface for submitting images for processing and viewing the results.

## Requirements

### Implementation Requirements

1. **Parallel Development Patterns:** At least two parallel development patterns must be integrated.
2. **Concurrency Mechanisms:** Use of threads/runnables, locks, and semaphores.
3. **Server Load Management:** Servers must update their load status in a `load.info` file, ensuring only one thread accesses this file at a time.
4. **Queue for Processing:** When a server exceeds its processing capacity, additional image parts must queue up for processing in a FIFO manner.
5. **TCP/IP Sockets:** Communication between clients and servers must use TCP/IP sockets.
6. **Image Reconstruction:** After processing, the image parts must be reassembled into the original image format and stored in the `results` directory with the format `<old-name>_edited`.
7. **User Interface:** A GUI for users to submit images and view processing results.
8. **Git Practices:** Follow best practices for collaborative development, including semantic commits and branch management.

### Extra Requirements (Optional)

1. **Scalability:** The system should easily scale with the addition of more servers without major changes or interruptions.
2. **Fault Tolerance:** Implement mechanisms to handle server failures and reallocate tasks to available servers seamlessly.

### Delivery Requirements

1. **Deadline:** The project must be submitted by March 23, 2024.
2. **Submission:** Submit through the course repository by creating a branch named `final`.
3. **Integration Tests:** Include integration tests and a code coverage report generated by JaCoCo.
4. **Documentation:** Provide documentation for all classes and methods using Javadoc.
5. **GitHub Actions:** Implement GitHub Actions for running integration tests, generating code coverage reports, and creating documentation.

## Usage Instructions

### Prerequisites

- Java Development Kit (JDK) 11 or higher
- Apache Maven
- Git

### Setup

1. **Clone the Repository:**

   ```sh
   git clone https://github.com/jpedro85/pa-distributed-img.git
   cd pa-distributed-img
   ```

2. **Build the Project:**

   ```sh
   mvn clean install
   ```

3. **Configure the Project:**
   - Edit the `config.ini` file to specify the number of servers and their processing capacities along with the starting port for sockets and the maximum amount of servers the user choose

### Running the Application

1. **Start the Servers:**

   ```sh
   java -jar server.jar
   ```

2. **Start the Client:**

   ```sh
   java -jar client.jar
   ```

### User Interface

- **Submit Images:** Use the GUI to select and submit images for processing.
- **View Logs:** Monitor the processing status and logs in the output window.
- **Manage Servers:** Add or remove servers and view their status in real-time.