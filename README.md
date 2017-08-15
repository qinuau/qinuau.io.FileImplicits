# qinuau.io.file.Files
This is library of Scala to use for files. 
## Usage
    import qinuau.io.FileImplicits._

    new File("/").walk(process = {
      (file) => println(file.toAbsolutePath().getParent().toString())
    })

It is way of all files from start path.

or

    new File("/").walk(maxDepth = 2, process = {
      (file) => println(file.toAbsolutePath().getParent().toString())
    })

input to maxDepth argument, it walk only number of depth. 
