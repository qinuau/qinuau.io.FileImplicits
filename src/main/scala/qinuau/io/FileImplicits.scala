package qinuau.io

import java.io.File
import java.io.IOException
import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes
import java.util.HashSet

object FileImplicits {
  implicit class FileImplicit(x: File) {
    var tmpTotalSize: Long = 0

    def newWalk(process: (Path) => Unit): Unit = {
      recursiveProcess(x.getCanonicalFile.toString, process)
    }

    def recursiveProcess(file: String, process: (Path) => Unit): Unit = {
      val f = new File(file)

      if (f.isDirectory) {
        f.listFiles.foreach {e => 
          recursiveProcess(e.getCanonicalFile.toString, process)
	}
      }
      else {
        val path = Paths.get(file)
        process(path)
      }
    }

    def newTotalSize = {
      newWalk(process = (p) => {

      })
    }

    def walk(maxDepth: Int = 0, process: (Path) => Unit) {
      val startPath = Paths.get(x.getCanonicalFile.toString)
      val options = new HashSet[FileVisitOption]

      if (maxDepth == 0) {
        Files.walkFileTree(startPath, new SimpleFileVisitor[Path] {
          override def visitFile(file: Path, attrs: BasicFileAttributes) = {
            process(file)
            FileVisitResult.CONTINUE
          }
  
          override def preVisitDirectory(file: Path, attrs: BasicFileAttributes) = {
            process(file)
            FileVisitResult.CONTINUE
          }
  
          override def visitFileFailed(file: Path, exc: IOException) = {
            FileVisitResult.SKIP_SUBTREE 
          }
        })
      }
      else {
        Files.walkFileTree(startPath, options, maxDepth, new SimpleFileVisitor[Path] {
          override def visitFile(file: Path, attrs: BasicFileAttributes) = {
            process(file)
            FileVisitResult.CONTINUE
          }
  
          override def preVisitDirectory(file: Path, attrs: BasicFileAttributes) = {
            process(file)
            FileVisitResult.CONTINUE
          }
  
          override def visitFileFailed(file: Path, exc: IOException) = {
            FileVisitResult.SKIP_SUBTREE 
          }
        })
      }
    }
  
    def getTotalSize: Long = {
      this.walk(process = {(file: Path) => 
        if (Files.isDirectory(file) == false) {
          tmpTotalSize += Files.size(file)
        }
      })
  
      val totalSize = tmpTotalSize
      tmpTotalSize = 0
  
      return totalSize
    }
  }
}

