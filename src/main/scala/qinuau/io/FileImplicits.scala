package qinuau.io

import java.io.File
import java.io.IOException
import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes
import java.util.HashSet

object FileImplicits {
  implicit class FileImplicit(x: File) {
    var tmpTotalSize: Long = 0
  
    def walk(maxDepth: Int = 0, process: (Path) => Unit) {
      var startPath = Paths.get(x.getCanonicalFile().toString())
      var options = new HashSet[FileVisitOption]()
  
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
  
    def getTotalSize(file: Path): Long = {
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

