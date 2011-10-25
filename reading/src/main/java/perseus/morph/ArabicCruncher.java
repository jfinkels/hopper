package perseus.morph;

import gpl.pierrick.brihaye.aramorph.Solution;

import perseus.morph.ArabicCodeGenerator;

import java.util.HashMap;
import java.util.Map;

public class ArabicCruncher {


	/**
	 * Convert an AraMorph morphological parse (a Solution) into a morphcode.
	 *
	 */
	 
	public static String getCode(Solution s) {

		ArabicCodeGenerator acg=new ArabicCodeGenerator();
		return acg.getCode(getFeatures(s));	
	
	}

	/**
	 * Convert an AraMorph morphological string into a feature Map.
	 *
	 * An AraMorph morph string takes the form:
	 * "and/so + will + they (people) <pos>fa/CONJ+sa/FUT+ya/IV3MP+</pos>"
	 *
	 */
		
	public static HashMap<String, String> getFeatures(String aramorph) {
	
		HashMap<String, String> features = new HashMap<String, String>();

		aramorph=aramorph.replaceAll(".*?([A-Z].*)", "$1");
		aramorph=aramorph.replaceAll("</pos>.*", "");

		String[] pos=aramorph.split("/");
		for (int i=0; i<pos.length; i++) {
			String tmp=pos[i];
			tmp=tmp.replaceAll("\\+.*$", "");
			features.putAll(convertPOS(tmp));
		}

		return features;

	}

	/**
	 * Convert an AraMorph morphological parse (a Solution) into a feature Map
	 * that we can use for a perseus.morph.Parse.
	 *
	 */
	public static HashMap<String, String> getFeatures(Solution s) {
	
		String[] POS;
		POS = s.getPrefixesArabicLongPOS();	
		
		HashMap<String, String> features = new HashMap<String, String>();
	
		if (POS != null) {		
			for (int i = 0 ; i < POS.length ; i++) {
				features.putAll(getFeatures(POS[i]));
			}		
		}
		features.putAll(getFeatures(s.getStemMorphology()));
		
		String stemPOS=s.getStemArabicLongPOS();
		if (stemPOS != null) {
			features.putAll(getFeatures(stemPOS));
		}
		
		POS = s.getSuffixesArabicLongPOS();
		
		if (POS != null) {		
			for (int i = 0 ; i < POS.length ; i++) {	
				features.putAll(getFeatures(POS[i]));
			}		
		}
		
		return features;
	
	}
	
	/**
	 * Convert an AraMorph-supplied part of speech string into a feature Map
	 *
	 */
	
	public static Map<String, String> convertPOS(String aramorph) {
	
		HashMap<String, String> features = new HashMap<String, String>();
		ArabicCodeGenerator acg=new ArabicCodeGenerator();
		
		if (aramorph.equals("ADJ")) { return acg.getFeatures("a------------"); }
		if (aramorph.equals("NOUN")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("PART")) { return acg.getFeatures("g------------"); }
		if (aramorph.equals("FUT_PART")) { return acg.getFeatures("g------------"); }
		if (aramorph.equals("INTERROG")) { return acg.getFeatures("------------i"); }
		if (aramorph.equals("INTERROG_PART")) { return acg.getFeatures("------------i"); }
		if (aramorph.equals("NEG_PART")) { return acg.getFeatures("------------n"); }
		
		if (aramorph.equals("ADV")) { return acg.getFeatures("d------------"); }
		if (aramorph.equals("FUNC_WORD")) { return acg.getFeatures("f------------"); }
		if (aramorph.equals("INTERJ")) { return acg.getFeatures("i------------"); }
		if (aramorph.equals("NOUN_PROP")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("NUMERIC_COMMA")) { return acg.getFeatures("m------------"); }
		if (aramorph.equals("VERB_IMPERFECT")) { return acg.getFeatures("v--i---------"); }
		if (aramorph.equals("DEM_PRON_F")) { return acg.getFeatures("a-----f------"); }
		if (aramorph.equals("DEM_PRON_FD")) { return acg.getFeatures("a-d---f------"); }
		if (aramorph.equals("DEM_PRON_FP")) { return acg.getFeatures("a-p---f------"); }
		if (aramorph.equals("DEM_PRON_FS")) { return acg.getFeatures("a-s---f------"); }
		if (aramorph.equals("DEM_PRON_MD")) { return acg.getFeatures("a-d---m------"); }
		if (aramorph.equals("DEM_PRON_MP")) { return acg.getFeatures("a-p---m------"); }
		if (aramorph.equals("DEM_PRON_MS")) { return acg.getFeatures("a-s---m------"); }
		if (aramorph.equals("REL_PRON")) { return acg.getFeatures("a------------"); }
		
		if (aramorph.equals("CASE_DEF_ACC")) { return acg.getFeatures("-------a--d--"); }
		if (aramorph.equals("CASE_DEF_GEN")) { return acg.getFeatures("-------g--d--"); }
		if (aramorph.equals("CASE_DEF_NOM")) { return acg.getFeatures("-------n--d--"); }
		if (aramorph.equals("CASE_INDEF_ACC")) { return acg.getFeatures("-------a--i--"); }
		if (aramorph.equals("CASE_INDEF_GEN")) { return acg.getFeatures("-------g--i--"); }
		if (aramorph.equals("CASE_INDEF_NOM")) { return acg.getFeatures("-------n--i--"); }
		if (aramorph.equals("CVSUFF_DO:1P")) { return acg.getFeatures("-----------a-"); }
		if (aramorph.equals("CVSUFF_DO:1S")) { return acg.getFeatures("-----------b-"); }
		if (aramorph.equals("CVSUFF_DO:3D")) { return acg.getFeatures("-----------h-"); }
		if (aramorph.equals("CVSUFF_DO:3FP")) { return acg.getFeatures("-----------i-"); }
		if (aramorph.equals("CVSUFF_DO:3FS")) { return acg.getFeatures("-----------j-"); }
		if (aramorph.equals("CVSUFF_DO:3MP")) { return acg.getFeatures("-----------k-"); }
		if (aramorph.equals("CVSUFF_DO:3MS")) { return acg.getFeatures("-----------l-"); }
		if (aramorph.equals("CVSUFF_SUBJ:2FS")) { return acg.getFeatures("-2s---f------"); }
		if (aramorph.equals("CVSUFF_SUBJ:2MP")) { return acg.getFeatures("-2p---m------"); }
		if (aramorph.equals("CVSUFF_SUBJ:2MS")) { return acg.getFeatures("-2s---p------"); }
		if (aramorph.equals("IVSUFF_DO:1P")) { return acg.getFeatures("-----------a-"); }
		if (aramorph.equals("IVSUFF_DO:1S")) { return acg.getFeatures("-----------b-"); }
		if (aramorph.equals("IVSUFF_DO:2D")) { return acg.getFeatures("-----------c-"); }
		if (aramorph.equals("IVSUFF_DO:2FP")) { return acg.getFeatures("-----------d-"); }
		if (aramorph.equals("IVSUFF_DO:2FS")) { return acg.getFeatures("-----------e-"); }
		if (aramorph.equals("IVSUFF_DO:2MP")) { return acg.getFeatures("-----------f-"); }
		if (aramorph.equals("IVSUFF_DO:2MS")) { return acg.getFeatures("-----------g-"); }
		if (aramorph.equals("IVSUFF_DO:3D")) { return acg.getFeatures("-----------h-"); }
		if (aramorph.equals("IVSUFF_DO:3FP")) { return acg.getFeatures("-----------i-"); }
		if (aramorph.equals("IVSUFF_DO:3FS")) { return acg.getFeatures("-----------j-"); }
		if (aramorph.equals("IVSUFF_DO:3MP")) { return acg.getFeatures("-----------k-"); }
		if (aramorph.equals("IVSUFF_DO:3MS")) { return acg.getFeatures("-----------l-"); }
		if (aramorph.equals("IVSUFF_SUBJ:2FS_MOOD:I")) { return acg.getFeatures("-2s-i-f------"); }
		if (aramorph.equals("IVSUFF_SUBJ:2FS_MOOD:SJ")) { return acg.getFeatures("-2s-s-f------"); }
		if (aramorph.equals("IVSUFF_SUBJ:3D")) { return acg.getFeatures("-3d----------"); }
		if (aramorph.equals("IVSUFF_SUBJ:3D_MOOD:I")) { return acg.getFeatures("-3d-i--------"); }
		if (aramorph.equals("IVSUFF_SUBJ:3FP")) { return acg.getFeatures("-3p---f------"); }
		if (aramorph.equals("IVSUFF_SUBJ:3MP_MOOD:I")) { return acg.getFeatures("-3p-i-m------"); }
		if (aramorph.equals("IVSUFF_SUBJ:3MP_MOOD:SJ")) { return acg.getFeatures("-3p-s-m------"); }
		if (aramorph.equals("IVSUFF_SUBJ:D_MOOD:I")) { return acg.getFeatures("--d-i--------"); }
		if (aramorph.equals("IVSUFF_SUBJ:D_MOOD:SJ")) { return acg.getFeatures("--d-s--------"); }
		if (aramorph.equals("IVSUFF_SUBJ:FP")) { return acg.getFeatures("--p---f------"); }
		if (aramorph.equals("IVSUFF_SUBJ:MP_MOOD:I")) { return acg.getFeatures("--p-i-m------"); }
		if (aramorph.equals("IVSUFF_SUBJ:MP_MOOD:SJ")) { return acg.getFeatures("--p-s-m------"); }
		if (aramorph.equals("NSUFF_FEM_DU_ACCGEN")) { return acg.getFeatures("--d---fz-----"); }
		if (aramorph.equals("NSUFF_FEM_DU_ACCGEN_POSS")) { return acg.getFeatures("--d---fz-p---"); }
		if (aramorph.equals("NSUFF_FEM_DU_NOM")) { return acg.getFeatures("--d---fn-----"); }
		if (aramorph.equals("NSUFF_FEM_DU_NOM_POSS")) { return acg.getFeatures("--d---fn-p---"); }
		if (aramorph.equals("NSUFF_FEM_PL")) { return acg.getFeatures("--p---f------"); }
		if (aramorph.equals("NSUFF_FEM_SG")) { return acg.getFeatures("--s---f------"); }
		if (aramorph.equals("NSUFF_MASC_DU_ACCGEN")) { return acg.getFeatures("--d---mz-----"); }
		if (aramorph.equals("NSUFF_MASC_DU_ACCGEN_POSS")) { return acg.getFeatures("--d---mz-p---"); }
		if (aramorph.equals("NSUFF_MASC_DU_NOM")) { return acg.getFeatures("--d---mn-----"); }
		if (aramorph.equals("NSUFF_MASC_DU_NOM_POSS")) { return acg.getFeatures("--d---mn-p---"); }
		if (aramorph.equals("NSUFF_MASC_PL_ACCGEN")) { return acg.getFeatures("--p---mz-----"); }
		if (aramorph.equals("NSUFF_MASC_PL_ACCGEN_POSS")) { return acg.getFeatures("--p---mz-p---"); }
		if (aramorph.equals("NSUFF_MASC_PL_NOM")) { return acg.getFeatures("--p---mn-----"); }
		if (aramorph.equals("NSUFF_MASC_PL_NOM_POSS")) { return acg.getFeatures("--p---mn-p---"); }
		if (aramorph.equals("NSUFF_MASC_SG_ACC_INDEF")) { return acg.getFeatures("--s---ma--i--"); }
		if (aramorph.equals("POSS_PRON_1P")) { return acg.getFeatures("---------a---"); }
		if (aramorph.equals("POSS_PRON_1S")) { return acg.getFeatures("---------b---"); }
		if (aramorph.equals("POSS_PRON_2D")) { return acg.getFeatures("---------c---"); }
		if (aramorph.equals("POSS_PRON_2FP")) { return acg.getFeatures("---------d---"); }
		if (aramorph.equals("POSS_PRON_2FS")) { return acg.getFeatures("---------e---"); }
		if (aramorph.equals("POSS_PRON_2MP")) { return acg.getFeatures("---------f---"); }
		if (aramorph.equals("POSS_PRON_2MS")) { return acg.getFeatures("---------g---"); }
		if (aramorph.equals("POSS_PRON_3D")) { return acg.getFeatures("---------h---"); }
		if (aramorph.equals("POSS_PRON_3FP")) { return acg.getFeatures("---------i---"); }
		if (aramorph.equals("POSS_PRON_3FS")) { return acg.getFeatures("---------j---"); }
		if (aramorph.equals("POSS_PRON_3MP")) { return acg.getFeatures("---------k---"); }
		if (aramorph.equals("POSS_PRON_3MS")) { return acg.getFeatures("---------l---"); }
		if (aramorph.equals("PRON_1P")) { return acg.getFeatures("-----------a-"); }
		if (aramorph.equals("PRON_1S")) { return acg.getFeatures("-----------b-"); }
		if (aramorph.equals("PRON_2D   ")) { return acg.getFeatures("-----------c-"); }
		if (aramorph.equals("PRON_2FP ")) { return acg.getFeatures("-----------d-"); }
		if (aramorph.equals("PRON_2FS    ")) { return acg.getFeatures("-----------e-"); }
		if (aramorph.equals("PRON_2MP   ")) { return acg.getFeatures("-----------f-"); }
		if (aramorph.equals("PRON_2MS    ")) { return acg.getFeatures("-----------g-"); }
		if (aramorph.equals("PRON_3D   ")) { return acg.getFeatures("-----------h-"); }
		if (aramorph.equals("PRON_3FP ")) { return acg.getFeatures("-----------i-"); }
		if (aramorph.equals("PRON_3FS")) { return acg.getFeatures("-----------j-"); }
		if (aramorph.equals("PRON_3MP")) { return acg.getFeatures("-----------k-"); }
		if (aramorph.equals("PRON_3MS    ")) { return acg.getFeatures("-----------l-"); }
		if (aramorph.equals("PVSUFF_DO:1P")) { return acg.getFeatures("-----------a-"); }
		if (aramorph.equals("PVSUFF_DO:1S")) { return acg.getFeatures("-----------b-"); }
		if (aramorph.equals("PVSUFF_DO:2D")) { return acg.getFeatures("-----------c-"); }
		if (aramorph.equals("PVSUFF_DO:2FP")) { return acg.getFeatures("-----------d-"); }
		if (aramorph.equals("PVSUFF_DO:2FS")) { return acg.getFeatures("-----------e-"); }
		if (aramorph.equals("PVSUFF_DO:2MP")) { return acg.getFeatures("-----------f-"); }
		if (aramorph.equals("PVSUFF_DO:2MS")) { return acg.getFeatures("-----------g-"); }
		if (aramorph.equals("PVSUFF_DO:3D")) { return acg.getFeatures("-----------h-"); }
		if (aramorph.equals("PVSUFF_DO:3FP")) { return acg.getFeatures("-----------i-"); }
		if (aramorph.equals("PVSUFF_DO:3FS")) { return acg.getFeatures("-----------j-"); }
		if (aramorph.equals("PVSUFF_DO:3MP")) { return acg.getFeatures("-----------k-"); }
		if (aramorph.equals("PVSUFF_DO:3MS")) { return acg.getFeatures("-----------l-"); }
		if (aramorph.equals("PVSUFF_SUBJ:1P")) { return acg.getFeatures("-1p----------"); }
		if (aramorph.equals("PVSUFF_SUBJ:1S")) { return acg.getFeatures("-1s----------"); }
		if (aramorph.equals("PVSUFF_SUBJ:2D")) { return acg.getFeatures("-2d----------"); }
		if (aramorph.equals("PVSUFF_SUBJ:2FP")) { return acg.getFeatures("-2p---f------"); }
		if (aramorph.equals("PVSUFF_SUBJ:2FS")) { return acg.getFeatures("-2s---f------"); }
		if (aramorph.equals("PVSUFF_SUBJ:2MP")) { return acg.getFeatures("-2p---m------"); }
		if (aramorph.equals("PVSUFF_SUBJ:2MS")) { return acg.getFeatures("-2s---m------"); }
		if (aramorph.equals("PVSUFF_SUBJ:3FD")) { return acg.getFeatures("-3d---f------"); }
		if (aramorph.equals("PVSUFF_SUBJ:3FP")) { return acg.getFeatures("-3p---f------"); }
		if (aramorph.equals("PVSUFF_SUBJ:3FS")) { return acg.getFeatures("-3s---f------"); }
		if (aramorph.equals("PVSUFF_SUBJ:3MD")) { return acg.getFeatures("-3d---m------"); }
		if (aramorph.equals("PVSUFF_SUBJ:3MP")) { return acg.getFeatures("-3p---m------"); }
		if (aramorph.equals("PVSUFF_SUBJ:3MS")) { return acg.getFeatures("-3s---m------"); }
		if (aramorph.equals("PREP")) { return acg.getFeatures("------------p"); }
		if (aramorph.equals("CONJ")) { return acg.getFeatures("------------c"); }
		if (aramorph.equals("DET")) { return acg.getFeatures("----------d--"); }
		if (aramorph.equals("EMPHATIC_PARTICLE")) { return acg.getFeatures("------------e"); }
		if (aramorph.equals("FUT")) { return acg.getFeatures("---f---------"); }
		if (aramorph.equals("IV1P")) { return acg.getFeatures("-1p----------"); }
		if (aramorph.equals("IV1S")) { return acg.getFeatures("-1s----------"); }
		if (aramorph.equals("IV2D")) { return acg.getFeatures("-2d----------"); }
		if (aramorph.equals("IV2FP")) { return acg.getFeatures("-2p---f------"); }
		if (aramorph.equals("IV2FS")) { return acg.getFeatures("-2s---f------"); }
		if (aramorph.equals("IV2MP")) { return acg.getFeatures("-2p---m------"); }
		if (aramorph.equals("IV2MS")) { return acg.getFeatures("-2s---m------"); }
		if (aramorph.equals("IV3FD")) { return acg.getFeatures("-3d---f------"); }
		if (aramorph.equals("IV3FP")) { return acg.getFeatures("-3p---f------"); }
		if (aramorph.equals("IV3FS")) { return acg.getFeatures("-3s---f------"); }
		if (aramorph.equals("IV3MD")) { return acg.getFeatures("-3d---m------"); }
		if (aramorph.equals("IV3MP")) { return acg.getFeatures("-3p---m------"); }
		if (aramorph.equals("IV3MS")) { return acg.getFeatures("-3s---m------"); }
		if (aramorph.equals("RESULT_CLAUSE_PARTICLE")) { return acg.getFeatures("------------r"); }
		if (aramorph.equals("SUBJUNC")) { return acg.getFeatures("----s--------"); }	
		if (aramorph.equals("CV")) { return acg.getFeatures("v---m--------"); }
		if (aramorph.equals("FW")) { return acg.getFeatures("f------------"); }
		if (aramorph.equals("FW-Wa")) { return acg.getFeatures("f------------"); }
		if (aramorph.equals("FW-Wa-A")) { return acg.getFeatures("f------------"); }
		if (aramorph.equals("FW-Wa-a")) { return acg.getFeatures("f------------"); }
		if (aramorph.equals("FW-Wa-i")) { return acg.getFeatures("f------------"); }
		if (aramorph.equals("FW-Wa-n~")) { return acg.getFeatures("f------------"); }
		if (aramorph.equals("FW-Wa-n~a")) { return acg.getFeatures("f------------"); }
		if (aramorph.equals("FW-Wa-o")) { return acg.getFeatures("f------------"); }
		if (aramorph.equals("FW-Wa-y")) { return acg.getFeatures("f------------"); }
		if (aramorph.equals("FW-WaBi")) { return acg.getFeatures("f------------"); }
		if (aramorph.equals("IV")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV-n")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV-n_Pass_yu")) { return acg.getFeatures("v--i-p-------"); }
		if (aramorph.equals("IV-n_intr")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV-n_no-Pref-A")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV-n_no-Pref-A_yu")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV-n_yu")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV-|")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_0")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_0_Pass_yu")) { return acg.getFeatures("v--i-p-------"); }
		if (aramorph.equals("IV_0_no-Pref-A")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_0hAnn")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_0hAnn_no-Pref-A")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_0hAnn_yu")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_0hwnyn")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_0hwnyn_no-Pref-A")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_0hwnyn_yu")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_Ann")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_Ann_Pass_yu")) { return acg.getFeatures("v--i-p-------"); }
		if (aramorph.equals("IV_Ann_no-Pref-A")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_C")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_C_Pass_yu")) { return acg.getFeatures("v--i-p-------"); }
		if (aramorph.equals("IV_C_intr")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_C_intr_yu")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_C_yu")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_Cn")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_Pass_yu")) { return acg.getFeatures("v--i-p-------"); }
		if (aramorph.equals("IV_V")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_V_Pass_yu")) { return acg.getFeatures("v--i-p-------"); }
		if (aramorph.equals("IV_V_intr")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_V_intr_no-Pref-A")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_V_intr_yu")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_V_no-Pref-A")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_V_yu")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_h")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_h_no-Pref-A")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_intr")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_intr_yu")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_need-Pref-A_yu")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_need-Pref-|")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_no-Pref-A")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_no-Pref-A_yu")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_wn")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_wn_yu")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_yn")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_yn_yu")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("IV_yu")) { return acg.getFeatures("v--i-a-------"); }
		if (aramorph.equals("N")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("N-ap")) { return acg.getFeatures("n-s---m------"); }
		if (aramorph.equals("N-|")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("N-|t")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("N/At")) { return acg.getFeatures("n-s---m------"); }
		if (aramorph.equals("N/ap")) { return acg.getFeatures("n-s---m------"); }
		if (aramorph.equals("N0")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("N0F")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("N0F_Nh")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("N0_L")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("N0_Nh")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("N0_Nhy")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("NAn_Nayn")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("NAt")) { return acg.getFeatures("n-p---f------"); }
		if (aramorph.equals("NF")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("NF_Nhy")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("NK")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("Nall")) { return acg.getFeatures("n-s---m------"); }
		if (aramorph.equals("Nap")) { return acg.getFeatures("n-s---f------"); }
		if (aramorph.equals("NapAt")) { return acg.getFeatures("n-s---f------"); }
		if (aramorph.equals("Napdu")) { return acg.getFeatures("n-s---f------"); }
		if (aramorph.equals("Nayn")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("Ndip")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("Ndu")) { return acg.getFeatures("n-s---m------"); }
		if (aramorph.equals("NduAt")) { return acg.getFeatures("n-s---m------"); }
		if (aramorph.equals("Nel")) { return acg.getFeatures("n-p---m------"); }
		if (aramorph.equals("Nh")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("Nh_Niyn")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("Nh_Nuwn")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("Nhy")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("Nprop")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("Numb")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("Nuwn_Niyn")) { return acg.getFeatures("n------------"); }
		if (aramorph.equals("PV")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV->")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV->_intr")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV-n")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV-n_intr")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV-t")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV-t_intr")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV-|")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV-|_intr")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_0")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_0h")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_Atn")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_C")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_C_Pass")) { return acg.getFeatures("v--r-p-------"); }
		if (aramorph.equals("PV_C_intr")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_Cn")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_Cn_intr")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_Ct")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_Ct_intr")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_Pass")) { return acg.getFeatures("v--r-p-------"); }
		if (aramorph.equals("PV_Pass-a")) { return acg.getFeatures("v--r-p-------"); }
		if (aramorph.equals("PV_Pass-aAat")) { return acg.getFeatures("v--r-p-------"); }
		if (aramorph.equals("PV_V")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_V_Pass")) { return acg.getFeatures("v--r-p-------"); }
		if (aramorph.equals("PV_V_intr")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_h")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_intr")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_no-w")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_no-w_Pass")) { return acg.getFeatures("v--r-p-------"); }
		if (aramorph.equals("PV_no-w_intr")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_ttAw")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_ttAw_intr")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_w")) { return acg.getFeatures("v--r-a-------"); }
		if (aramorph.equals("PV_w_intr")) { return acg.getFeatures("v--r-a-------"); }

		/**
		 * Return empty hashmap if no features are found.
		 *
		 */
		 
		return new HashMap<String, String>();
	
	
	}

}