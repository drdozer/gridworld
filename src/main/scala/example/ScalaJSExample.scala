package example

import scala.scalajs.js
import js.annotation.JSExport

import org.scalajs.dom._

@JSExport
object ScalaJSExample {
  @JSExport
  def main(): Unit = {
    val paragraph = document.createElement("p")
    paragraph.innerHTML = "<strong>It works!</strong>"
    document.getElementById("playground").appendChild(paragraph)
  }

  @JSExport
  def makeGridWorld(id: String): Unit = {
    println("makeGridWorld")
    val svg = document.getElementById(id).asInstanceOf[SVGSVGElement]
    val rows = svg.getAttribute("rows").toInt
    val cols = svg.getAttribute("cols").toInt
    val rowHeight = svg.height.baseVal.value / rows
    val colWidth = svg.width.baseVal.value / cols

    println(f"dims: $rows $cols")

    for(r <- 0 until rows;
        c <- 0 until cols) {
      val cell = document.createElementNS("http://www.w3.org/2000/svg", "rect")
      println(f"Created rect element: $cell")
      cell.setAttribute("x", (c * colWidth).toString)
      cell.setAttribute("y", (r * rowHeight).toString)
      cell.setAttribute("width", colWidth.toString)
      cell.setAttribute("height", rowHeight.toString)
      cell.setAttribute("class", f"cell r_$r c_$c")
      svg.appendChild(cell)
    }
  }

  /** Computes the square of an integer.
   *  This demonstrates unit testing.
   */
  def square(x: Int): Int = x*x
}
