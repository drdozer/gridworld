package example

import scala.scalajs.js
import js.annotation.JSExport

import org.scalajs.dom._
import scala.util.Random

import org.scalajs.jquery._
import scala.scalajs.js.RegExp

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
    val svg = document.getElementById(id).asInstanceOf[SVGSVGElement]
    val rows = svg.getAttribute("rows").toInt
    val cols = svg.getAttribute("cols").toInt
    val rowHeight = svg.height.baseVal.value / rows
    val colWidth = svg.width.baseVal.value / cols
    val rowHeightHalf = rowHeight / 2
    val colWidthHalf = colWidth / 2

    val boxes = document.createElementNS(svgNS, "g")
    boxes.setAttribute("class", "boxes")
    svg.appendChild(boxes)

    val critters = document.createElementNS(svgNS, "g")
    critters.setAttribute("class", "critters")
    svg.appendChild(critters)

    for(r <- 0 until rows;
        c <- 0 until cols)
    {
      val box = document.createElementNS(svgNS, "rect")
      box.setAttribute("x", (c * colWidth).toString)
      box.setAttribute("y", (r * rowHeight).toString)
      box.setAttribute("width", colWidth.toString)
      box.setAttribute("height", rowHeight.toString)
      box.setAttribute("class", f"box r_$r c_$c")
      boxes.appendChild(box)

      val critter = document.createElementNS(svgNS, "ellipse")
      critter.setAttribute("cx", (c*colWidth + colWidthHalf).toString)
      critter.setAttribute("cy", (r * rowHeight + rowHeightHalf).toString)
      critter.setAttribute("rx", (colWidthHalf * 0.8).toString)
      critter.setAttribute("ry", (rowHeightHalf * 0.8).toString)
      critter.setAttribute("class", f"critter r_$r c_$c dead")
      critters.appendChild(critter)
    }

  }

  @JSExport
  def animateGridWorld(id: String, animationRate: Long, changeColorRate: Double) = {
    val svg = document.getElementById(id).asInstanceOf[SVGSVGElement]
    val rows = svg.getAttribute("rows").toInt
    val cols = svg.getAttribute("cols").toInt
    val critters = svg.querySelector(".critters")



    def animateCritters(): Unit = {
      val r = rand.nextInt(rows)
      val c = rand.nextInt(cols)
      val critter = critters.querySelector(f".critter.r_$r.c_$c")
      if(rand.nextDouble() < changeColorRate) {
        def randByte = rand.nextInt(255)

        critter.setAttribute("fill", f"rgb($randByte,$randByte,$randByte)")
      } else {
        val jCritter = jQuery(critter)
        if (jCritter.hasClass("alive")) {
          jCritter.removeClass("alive").addClass("dead")
        } else {
          jCritter.removeClass("dead").addClass("alive")
        }
      }
    }

    window.setInterval(animateCritters _, animationRate)
  }

  @JSExport
  def setCritters(id: String, settings: js.Dictionary[js.Any]*): Unit = {
    val svg = document.getElementById(id).asInstanceOf[SVGSVGElement]
    val critters = svg.querySelector(".critters")

    for(s <- settings) {
      val r = s("r").asInstanceOf[js.Number]
      val c = s("c").asInstanceOf[js.Number]
      val color = s("color").asInstanceOf[js.String]

      val critter = critters.querySelector(f".critter.r_$r.c_$c")
      if(color != null) critter.setAttribute("fill", color)

      val state = s("state").asInstanceOf[js.String]
      val DEAD : js.String = "dead"
      val ALIVE : js.String = "alive"
      state match {
        case ALIVE =>
          critter.removeClass("dead").addClass("alive")
        case DEAD =>
          critter.removeClass("alive").addClass("dead")
        case _ =>
      }
    }
  }

  @JSExport
  def specialHug(id: String, p1: js.Dictionary[js.Any], p2: js.Dictionary[js.Any], c1: js.Dictionary[js.Any]): Unit = {
    val g = js.Dynamic.global
    val pusher = g.pusher

    val grid = document.querySelector("#grid")
    val p1El = grid.querySelector(f".critter.r_${p1("r")}.c_${p1("c")}")
    val p2El = grid.querySelector(f".critter.r_${p2("r")}.c_${p2("c")}")
    val c1El = grid.querySelector(f".critter.r_${c1("r")}.c_${c1("c")}")

    val p1_cx: Double = p1El.getAttribute("cx").asInstanceOf[js.Number]
    val p1_cy: Double = p1El.getAttribute("cy").asInstanceOf[js.Number]

    val p2_cx: Double = p2El.getAttribute("cx").asInstanceOf[js.Number]
    val p2_cy: Double = p2El.getAttribute("cy").asInstanceOf[js.Number]

    val c1_cx: Double = c1El.getAttribute("cx").asInstanceOf[js.Number]
    val c1_cy: Double = c1El.getAttribute("cy").asInstanceOf[js.Number]

    val p1_cx_anim = p1_cx * 2.0 / 3.0 + p2_cx * 1.0 / 3.0
    val p2_cx_anim = p1_cx * 1.0 / 3.0 + p2_cx * 2.0 / 3.0

    val p1_p2_mid_x = p1_cx / 2.0 + p2_cx / 2.0
    val p1_p2_mid_y = p1_cy / 2.0 + p2_cy / 2.0

    val p1_cy_anim = p1_cy * 2.0 / 3.0 + p2_cy * 1.0 / 3.0
    val p2_cy_anim = p1_cy * 1.0 / 3.0 + p2_cy * 2.0 / 3.0

    val p1_fill = pusher.color(p1El.getAttribute("fill"))
    val p2_fill = pusher.color(p2El.getAttribute("fill"))

    val c1_fill = p1_fill.blend(p2_fill, 0.5)

    println(f"p1_cx: $p1_cx p2_cx: $p2_cx p1_cx_anim: $p1_cx_anim p2_cx_anim: $p2_cx_anim p1_fill: ${p1_fill.html()} p2_fill: ${p2_fill.html()} c1_fill: ${c1_fill.html()}")

    def step1() {
      g.Snap(p1El).animate(js.Dynamic.literal(cx = p1_cx_anim, cy = p1_cy_anim), 1500)
      g.Snap(p2El).animate(js.Dynamic.literal(cx = p2_cx_anim, cy = p2_cy_anim), 1500, null, step2 _)
    }

    def step2() {
      c1El.removeClass("dead").addClass("alive")
      g.Snap(c1El).attr(js.Dynamic.literal(cx = p1_p2_mid_x, cy = p1_p2_mid_y, fill = c1_fill.html()))
      g.Snap(c1El).animate(js.Dynamic.literal(cx = c1_cx, cy = c1_cy), 1500, null, step3 _)
    }

    def step3() {
      g.Snap(p1El).animate(js.Dynamic.literal(cx = p1_cx, cy = p1_cy), 500)
      g.Snap(p2El).animate(js.Dynamic.literal(cx = p2_cx, cy = p2_cy), 500)
    }

    step1()
  }

  implicit class ElementPimp(val el : Element) extends AnyVal {
    def removeClass(cls: js.String): el.type = {
      val clss = el getAttribute "class"
      val clsArray = clss split (new RegExp("""\s+"""))
      val without = clsArray filter { (c: js.String) =>
        c != cls}
      el setAttribute ("class", without mkString " ")
      el
    }

    def addClass(cls: js.String): el.type = {
      val clss = el getAttribute "class"
      val clsArray = clss split (new RegExp("""\s+"""))
      if(!(clsArray contains cls)) {
        el setAttribute ("class", clsArray :+ cls mkString " ")
      }
      el
    }
  }
}