# FileOperation
Three methods of file segmentation and merging in Java

**Results**
-------------------
| Method               | Size   | Segment | Merge |
| :---                 | :--:   | :-----: | :---: |
| IO                   | 38.7MB |   287s  | 285s  |
| BufferedIO           | 208MB  |   7s    | 3s    |
| NIO(MappedByteBuffer)| 208MB  | < 1s    | < 1s  |

> **Note:** Time is for reference only, and is different according to the machine environment.

**Start**
-------------

 1. run fileoperation -> **FileGUI.java** to start the project.
 2.  You can change the method in FileGUI.java. **0** means **InputStream** and **OutputStream**,1 means **BufferedInputStream** and **BufferedOutputStream**,2 means **MappedByteBuffer**.
 
```java  
        public static void main(String[] args) throws Exception {
          //new FileGUI(0);//IO
            new FileGUI(1);//BufferedIO
          //new FileGUI(2);//NIO
        }
```
