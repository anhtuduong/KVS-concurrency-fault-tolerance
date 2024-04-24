Exercise KVStore, Concurrency and Fault-tolerance
Story

The German Meteorological Service (DWD) needs IT assistance for their weather station data storage. They collect hourly data from stations across Germany, storing it in an Append-Only Log. This data is used internally for forecasting and shared with other European agencies. The IT departement has already started designing the system that will handle the weather data. This figure presents an overview of their idea:

    Stations: Located at the top, each station sends data.
    DataLogger: Polls each station and sends the data to the central servers of the DWD.
    Key-Value Store: Stores the data received. Agencies, including the DWD, can access this data.

They need your help with four different parts of the system.

Note: The DWD has provided you with a incomplete implementation. There is #TODO indications where you should modify the code. MODIFY THE REST OF CODE AT YOUR OWN RISK.

Testing: The main function contains a test scenario for each task. It is not as complete as the grading tests but it should give you a way to easily test the tasks.
1. Data Processing

To gain statistical insights into the data, the DWD wants to calculate the average of the most recent numberOfHours values. Your task is to implement a simple average function.
calculateAverage()

Implement calculateAverage(int numberOfHours) in Entry.java to find the average temperature of the most recent hours. The function should calculate the average of the latest numberOfHours entries in the values list.

    If fewer than numberOfHours values exist, return the average of available values (wrapped inside an Optional), or Optional.empty() if no values exist or if the numberOfHours value is not positive.

    Suppose numberOfHours=n\text{numberOfHours}=nnumberOfHours=n. The arithmetic average of the values x[1],…,x[n]x[1],\dots,x[n]x[1],…,x[n] is: x[1]+⋯+x[n]n.\frac{x[1]+\dots+x[n]}{n}.nx[1]+⋯+x[n]​.

Example: If values = [4.0, 5.0. 8.0], then calculateAverage(2) should return Optional.of(4.5), as (4.0+5.0)/2=4.5.

Hint: Keep in mind in which order addTemperature(double temperature) inserts the values. Also, if you are not familiar with Optional<>, you might want to look at the following Java-Doc.
2. Key Value Store (Binary Search Tree)

Before the DWD can calculate the average temperature for a specific station, they need to store the data in an efficient structure. The designers have chosen to use a binary search tree (BST) indexed by station ID. You need to implement the following methods in Tree.java:

2.1. putTemperature()

The putTemperature(int stationId, double temperature) method manages temperature data in a Binary Search Tree (BST). Here's how it works:
1. Storing Temperature:

    If there is no existing Entry object for the given stationId, create a new Entry object and store the temperature.
    If there is already an Entry object for the given stationId, update this existing object with the new temperature.

2. Binary Search Tree Properties:

    Each Tree node's key (which is the stationId) must be strictly greater than the keys in its left subtree and strictly smaller than the keys in its right subtree.
    Do not create a new Tree node if one for the given stationId already exists; simply update the existing node.

For more information, you can visit https://en.wikipedia.org/wiki/Binary_search_tree.
2.2. getTreeNode()

The DWD has already implemented getAverage(int stationId, int numberOfHours). This method is called by the Agencies to get the data from the Key-Value Store.

You need to fill the method called getTreeNode(int stationId). This method searches the tree for the Entry that has the same ID as stationId. It should go through the tree (starting from the root) and return the node assoicated with the stationId.

If the station with the given stationId is not found in the tree, the method should return null.
2.3. getParentNode()

The DWD has noticed that the values of a parent node are statistically similar to the values of its two children. Therefore, the DWD wants to access the parent of a particular node as well.

This getParentNode(int stationId) should similarly to getTreeNode(). Given stationId, it should return the (unique) parent of the node associated with the stationId. If the station has no parent or the station is not found in the tree, the method should return null.
3. Concurrent Access To The Store

In Germany, there are numerous weather stations, each equipped with a corresponding DataLogger. Additionally, there are multiple Agency objects, operating within their own threads, which need access to the centralized data.


To ensure proper data management without directly exposing the tree to threads, the DWD has designated the KVStore class as the central interface. Your task is to use the given rwLock within KVStore to prevent concurrency issues during data insertion. Therefore, you have to implement the following three thread-safe methods:
3.1. putTemperature() (replaces unsafePutTemperature())
3.2. getAverage() (replaces unsafeGetAverage())
3.3. getAverageParent() (replace unsafeGetAverageParent())

You should utilize the provided ReadWriteLockDWD reader-writer lock for synchronization, using its four methods: lockWriteLock(), lockReadLock(), unlockWriteLock(), and unlockReadLock() in the methods putTemperature(), getAverage() around the calls to the unsafe_* methods (in KVStore.java) Important: If no DataLogger thread is currently writing new data, multiple agents should be able to simultaneously access the data for reading purposes. Hint: You can use the old unsafe methods in your implementation!
4. Fault-Tolerance

The DWD operates an alert system for hazardous weather conditions, requiring the latest data from all stations at all times. Occasionally, stations may fail. In such cases, DataLogger threads should try to get any data that can be relevant regarding the station.


If a station is failing, the DataLogger should retrieve the latest value of the parent entry. It should then insert the value as usual with putTemperature(). If the value of the parent entry is invalid as well, nothing should be inserted and the thread should continue running. Complete the code in the run() method of the DataLogger-class.

Hint: The only way to access data from the memory (Tree) is through getAverageParent(int stationId, int numberOfHours) of KVStore. Think about which value numberOfHours has to be to get the latest value of the parent entry.