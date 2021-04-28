# Digital System Project by Varut
The system is design to generalise the relationship of a dataset with two models of backpropagation and genetic algorithm being available.
This GitHub contain a Java Web system that demonstrate of the power of backpropagation and genetic algorithm. 

How to change dataset?

To change dataset just replace ‘dataset.txt’ but keep the name ‘dataset.txt’ with the value being predicted at the end and the date at the beginning. Please follow the format in the ‘dataset.txt’ (dataset source: https://datahub.io/core/s-and-p-500#resource-data. ) and replace the value in ‘nextMonthdata.txt’ with feature sets from the next month data unseen by the system.

How to train GA?
Please make sure the dataset is correct and select the train GA page and enters the minimum normalised desired error, mutation rate, number of layers in the system including input and output layers, crossover rate and generation without improvement. After all the inputs is correct the system will train until it is stuck in a local optimum for number of generations without improvement or the validation dataset error overtaken the training set. 
How to train BP?

Please make sure the dataset is correct and select the train BP page and enters the minimum normalised desired error, learning rate, number of layers in the system including input and output layers, epoch without improvement. After all the inputs is correct the system will train until it is stuck in a local optimum for number of epochs without improvement or the validation dataset error overtaken the training set. 

The purpose of this project?
The purpose of this project is to assist the user to make an informed decision about the price of the stock based on their underlying assets and the historic prices. This is only a guideline as stock market is filled with human actors that do not necessarily followed logical reasoning. The one constant is true is if there is a mismatch in the underlying assets and price, eventually there will be a correction, for example the .COM bubble (more information: Hayes, A., 2019. What Ever Happened to the Dotcom Bubble?. [online] Investopedia. Available at: <https://www.investopedia.com/terms/d/dotcom-bubble.asp> [Accessed 20 April 2021]. )

Thank you for viewing my project
