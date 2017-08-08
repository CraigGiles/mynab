package com.gilesc
package generator

import org.scalacheck.Gen
import org.scalacheck.Arbitrary

trait DateGenerator {
  import java.time._

  def genLocalDate(minYear: Year = Year.parse("1970")): Gen[LocalDate] = for {
    year <- Gen.choose(minYear.getValue, LocalDate.now().getYear())
    month <- Gen.choose(1, 12)
    day <- Gen.choose(1, YearMonth.of(year, month).lengthOfMonth())
  } yield LocalDate.parse(s"${"%04d".format(year)}-${"%02d".format(month)}-${"%02d".format(day)}")

  implicit def arbitraryLocalDate: Arbitrary[LocalDate] = Arbitrary(genLocalDate())
  implicit def arbitraryLocalDateString: Arbitrary[String] = Arbitrary(genLocalDate().sample.get.toString)
}

