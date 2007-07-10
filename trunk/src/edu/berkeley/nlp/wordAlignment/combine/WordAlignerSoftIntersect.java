package edu.berkeley.nlp.wordAlignment.combine;

import edu.berkeley.nlp.mt.Alignment;
import edu.berkeley.nlp.mt.SentencePair;
import edu.berkeley.nlp.wordAlignment.WordAligner;

/**
 * Soft intersection corresponds to thresholding the product of posteriors for each link.
 */
public class WordAlignerSoftIntersect extends WordAlignerCombined {
	private static final long serialVersionUID = 1L;

	public WordAlignerSoftIntersect(WordAligner wa1, WordAligner wa2) {
		super(wa1, wa2);
		this.modelPrefix = "int-soft-" + wa1.getModelPrefix() + "+" + wa2.getModelPrefix();
	}

	public String getName() {
		//		String s = usePosteriorDecodingFlag ? ", posteriorThreshold="
		//				+ posteriorDecodingThreshold : "";
		return "IntersectSoft(" + wa1.getName() + ", " + wa2.getName() + ")";
	}

	Alignment combineAlignments(Alignment a1, Alignment a2, SentencePair sentencePair) {
		Alignment a3;
		if (!usePosteriorDecodingFlag) {
			a3 = a1.intersect(a2);
		} else {
			int I = sentencePair.getEnglishWords().size();
			int J = sentencePair.getForeignWords().size();
			double[][] posteriors = new double[J][I];

			for (int j = 0; j < J; j++) {
				for (int i = 0; i < I; i++) {
					posteriors[j][i] = Math.sqrt(a1.getStrength(i, j) * a2.getStrength(i, j));
				}
			}
			a3 = a1.thresholdPosteriors(posteriors, posteriorDecodingThreshold);
		}
		return a3;
	}

}
