package org.mg4news.Doccy

object TestData {
  // Full set, duplicate names, different descriptions
  val full: Seq[(String,String)] = Seq(
    ("alpha", "first greek letter"),
    ("beta", "second greek letter"),
    ("phi", "another greek letter"),
    ("beta", "pretty sure its the second one"),
    ("phi", "definitely greek to me"),
    ("zeta", "the last letter, yes its greek")
  )

  // The duplicates are removed, only the final expected set remains
  val dedup: Seq[(String,String)] = Seq(
    ("alpha", "first greek letter"),
    ("beta", "pretty sure its the second one"),
    ("phi", "definitely greek to me"),
    ("zeta", "the last letter, yes its greek")
  )

  val bad: Seq[(String,String)] = Seq(
    ("delta", " a greek letter we dont use"),
    ("epsilon", "antoher letter we dont use"),
    ("kanji", "not even a greek letter")
  )

  val full_size = full.length
  val dedup_size = dedup.length
}
