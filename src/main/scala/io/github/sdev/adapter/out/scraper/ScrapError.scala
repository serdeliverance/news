package io.github.sdev.scraper

import net.ruippeixotog.scalascraper.model.Element

case class ScrapError(element: Element, message: String)
