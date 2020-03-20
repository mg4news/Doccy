package org.mg4news.Doccy

object TestData {

  // Shared bad names, never used in any of the tests anywhere.
  // Negative test
  val bad: Seq[String] = Seq("delta", "epsilon", "iota", "doc99", "doc0")

  // name::desc Full set, duplicate names, different descriptions
  val full: Seq[(String,String)] = Seq(
    ("alpha", "first greek letter"),
    ("beta", "second greek letter"),
    ("phi", "another greek letter"),
    ("beta", "pretty sure its the second one"),
    ("phi", "definitely greek to me"),
    ("zeta", "the last letter, yes its greek")
  )

  // name::desc, The duplicates are removed, only the final expected set remains
  val dedup: Seq[(String,String)] = Seq(
    ("alpha", "first greek letter"),
    ("beta", "pretty sure its the second one"),
    ("phi", "definitely greek to me"),
    ("zeta", "the last letter, yes its greek")
  )

  // / some pre-calculated sizes
  val full_size = full.length
  val dedup_size = dedup.length

  // DocDoc good data
  val docs_with_dups = Seq(
    ("Doc1", "Doc about 1", "bob", "proj1", "cat1", Seq("Top1", "Top2")),
    ("Doc2", "Doc about 2", "jim", "proj2", "cat2", Seq("Top2", "Top3")),
    ("Doc3", "Doc about 3", "sam", "proj1", "cat3", Seq("Top1", "Top3")),
    ("Doc4", "Doc about 4", "bob", "proj2", "cat1", Seq("Top1", "Top2", "Top3")),
    ("Doc5", "Doc about 5", "jim", "proj1", "cat2", Seq("Top1")),
    ("Doc6", "Doc about 6", "sam", "proj2", "cat3", Seq("Top3")),
    ("Doc1", "Doc about 1", "bob", "proj1", "cat1", Seq("Top2")),
    ("Doc4", "Doc about 4", "bob", "proj2", "cat1", Seq("Top2", "Top3"))
  )

  val docs_no_dups = Seq(
    ("Doc2", "Doc about 2", "jim", "proj2", "cat2", Seq("Top2", "Top3")),
    ("Doc3", "Doc about 3", "sam", "proj1", "cat3", Seq("Top1", "Top3")),
    ("Doc5", "Doc about 5", "jim", "proj1", "cat2", Seq("Top1")),
    ("Doc6", "Doc about 6", "sam", "proj2", "cat3", Seq("Top3")),
    ("Doc1", "Doc about 1", "bob", "proj1", "cat1", Seq("Top2")),
    ("Doc4", "Doc about 4", "bob", "proj2", "cat1", Seq("Top2", "Top3"))
  )
}
