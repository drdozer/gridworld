package example

import scala.scalajs.js
import js.annotation.JSExport

import org.scalajs.dom._
import scala.util.Random

@JSExport
object ScalaJSExample {
  val svgNS = "http://www.w3.org/2000/svg"
  val rand = new Random()

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
    val rowHeightHalf = rowHeight / 2
    val colWidthHalf = colWidth / 2

    println(f"dims: $rows $cols")

    val cells = document.createElementNS(svgNS, "g")
    cells.setAttribute("class", "cells")
    svg.appendChild(cells)

    val critters = document.createElementNS(svgNS, "g")
    critters.setAttribute("class", "critters")
    svg.appendChild(critters)

    for(r <- 0 until rows;
        c <- 0 until cols)
    {
      val cell = document.createElementNS(svgNS, "rect")
      println(f"Created rect element: $cell")
      cell.setAttribute("x", (c * colWidth).toString)
      cell.setAttribute("y", (r * rowHeight).toString)
      cell.setAttribute("width", colWidth.toString)
      cell.setAttribute("height", rowHeight.toString)
      cell.setAttribute("class", f"cell r_$r c_$c")
      cells.appendChild(cell)

      val critter = document.createElementNS(svgNS, "ellipse")
      critter.setAttribute("cx", (c*colWidth + colWidthHalf).toString)
      critter.setAttribute("cy", (r * rowHeight + rowHeightHalf).toString)
      critter.setAttribute("rx", (colWidthHalf * 0.8).toString)
      critter.setAttribute("ry", (rowHeightHalf * 0.8).toString)
      val status = if(rand.nextDouble > 0.6) "dead" else "alive"
      critter.setAttribute("class", f"critter r_$r c_$c $status")
      critters.appendChild(critter)
    }

    document.
  }

  /** Computes the square of an integer.
   *  This demonstrates unit testing.
   */
  def square(x: Int): Int = x*x
}
