package note.cmd;


import x.XApp;
import x.XLoggableCmd;

import java.util.*;
import note.noteApp;
import note.noteFormula;
import note.noteFormulaAtom;
import note.noteFormulaEdge;
import note.noteFormulaMgr;

public class noteCmdToDeleteEdge extends XLoggableCmd {
    private noteFormulaEdge edgeToDelete;

    // private constructor
    private noteCmdToDeleteEdge(XApp app, noteFormulaEdge edge) {
        super(app);
        this.edgeToDelete = edge;
    }

    public static boolean execute(XApp app, noteFormulaEdge edge) {
        noteCmdToDeleteEdge cmd = new noteCmdToDeleteEdge(app, edge);
        return cmd.execute();
    }

    @Override
    protected boolean defineCmd() {
        noteApp note = (noteApp) this.mApp;
        noteFormulaMgr formulaMgr = note.getFormulaMgr();

        // 1. 엣지 삭제
        noteFormula formula = formulaMgr.findFormulaFor(edgeToDelete);
        if (formula == null) {
            return false; // 엣지가 속한 Formula를 찾지 못함
        }
        formula.getEdges().remove(edgeToDelete);

        // 2. DFS로 연결된 원자 그룹 찾기
        Set<noteFormulaAtom> group1 = new HashSet<>();
        Set<noteFormulaAtom> group2 = new HashSet<>();
        findDisconnectedGroups(formula, group1, group2);

        // 3. 두 그룹으로 나누어진 경우 처리
        if (!group1.isEmpty() && !group2.isEmpty()) {
            // 새 Formula 생성 및 group2 이동
            noteFormula newFormula = new noteFormula();
            for (noteFormulaAtom atom : group2) {
                newFormula.getAtoms().add(atom);
                formula.getAtoms().remove(atom);
            }
            for (noteFormulaEdge edge : new ArrayList<>(formula.getEdges())) {
                if (group2.contains(edge.getStartAtom()) || group2.contains(edge.getEndAtom())) {
                    newFormula.getEdges().add(edge);
                    formula.getEdges().remove(edge);
                }
            }
            formulaMgr.getFormulas().add(newFormula);
            
            // 4. `editingFormula` 업데이트
            if (formula == formulaMgr.getEditingFormula()) {
                if (formula.getEdges().isEmpty() && newFormula.getEdges().isEmpty()) {
                    formulaMgr.setEditingFormula(null);
                    formulaMgr.removeFormula(formula);
                    formulaMgr.removeFormula(newFormula);
                } else if (formula.getEdges().isEmpty()) {
                    formulaMgr.setEditingFormula(newFormula); // 기존 Formula가 비어 있으면 새 Formula를 편집 대상으로 설정
                    formulaMgr.removeFormula(formula);
                } else if (newFormula.getEdges().isEmpty()) {
                    formulaMgr.setEditingFormula(formula); // 새 Formula가 비어 있으면 기존 Formula 유지
                    formulaMgr.removeFormula(newFormula);
                }
            }
        }
        

        return true;
    }

    private void findDisconnectedGroups(noteFormula formula, Set<noteFormulaAtom> group1, Set<noteFormulaAtom> group2) {
        // Formula에서 모든 Atom 가져오기
        List<noteFormulaAtom> atoms = formula.getAtoms();
        if (atoms.isEmpty()) {
            return;
        }

        // DFS를 사용하여 첫 번째 그룹 탐색
        Stack<noteFormulaAtom> stack = new Stack<>();
        Set<noteFormulaAtom> visited = new HashSet<>();

        stack.push(atoms.get(0));
        while (!stack.isEmpty()) {
            noteFormulaAtom curr = stack.pop();
            if (!visited.contains(curr)) {
                visited.add(curr);
                group1.add(curr);

                // 현재 Atom과 연결된 모든 Atom 탐색
                for (noteFormulaEdge edge : formula.getEdges()) {
                    if (edge.getStartAtom() == curr && !visited.contains(edge.getEndAtom())) {
                        stack.push(edge.getEndAtom());
                    }
                    if (edge.getEndAtom() == curr && !visited.contains(edge.getStartAtom())) {
                        stack.push(edge.getStartAtom());
                    }
                }
            }
        }

        // 나머지 Atom은 두 번째 그룹에 추가
        for (noteFormulaAtom atom : atoms) {
            if (!visited.contains(atom)) {
                group2.add(atom);
            }
        }
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        sb.append("Deleted Edge: ").append(edgeToDelete);
        return sb.toString();
    }
}
