#!/bin/bash

# ==============================================================================
# Batch Grading Script for Homework 2: Event & Ticket Management System
# ==============================================================================
#
# This script:
# 1. Compiles the auto-grader
# 2. Iterates through all student submissions (S_01 to S_25)
# 3. Runs the auto-grader for each student
# 4. Aggregates results into a single CSV file
# 5. Generates a defense list for suspicious submissions
#
# Usage: ./grade_all.sh
#
# ==============================================================================

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
SUBMISSIONS_DIR="../submissions"
RESULTS_DIR="./results"
AUTO_GRADER="EventSystemAutoGrader.java"
FINAL_CSV="$RESULTS_DIR/homework2_results.csv"
DEFENSE_LIST="$RESULTS_DIR/DEFENSE_REQUIRED.txt"

# ==============================================================================
# Setup
# ==============================================================================

echo -e "${BLUE}===============================================${NC}"
echo -e "${BLUE}   Homework 2 - Batch Grading System${NC}"
echo -e "${BLUE}===============================================${NC}"
echo ""

# Create results directory if it doesn't exist
mkdir -p "$RESULTS_DIR"

# Clean previous results
if [ -f "$FINAL_CSV" ]; then
    echo -e "${YELLOW}Removing previous results...${NC}"
    rm "$FINAL_CSV"
fi

if [ -f "$DEFENSE_LIST" ]; then
    rm "$DEFENSE_LIST"
fi

# ==============================================================================
# Compile Auto-Grader
# ==============================================================================

echo -e "${BLUE}Compiling auto-grader...${NC}"
javac "$AUTO_GRADER"

if [ $? -ne 0 ]; then
    echo -e "${RED}✗ Failed to compile auto-grader${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Auto-grader compiled successfully${NC}"
echo ""

# ==============================================================================
# Create CSV Header
# ==============================================================================

echo "Assignment;Type;Student;Compilation;PartA;PartB;PartC;PartD;Main;BaseScore;MaxBaseScore;Bonus;TotalScore;Percentage;Comments" > "$FINAL_CSV"

# ==============================================================================
# Grade All Students
# ==============================================================================

echo -e "${BLUE}Grading student submissions...${NC}"
echo ""

STUDENTS_GRADED=0
STUDENTS_FAILED=0
TOTAL_STUDENTS=25

for STUDENT_NUM in $(seq -f "%02g" 1 25); do
    STUDENT_ID="S_$STUDENT_NUM"
    STUDENT_DIR="$SUBMISSIONS_DIR/$STUDENT_ID"
    STUDENT_CSV="$RESULTS_DIR/${STUDENT_ID}_report.csv"

    echo -e "${BLUE}[$STUDENT_NUM/25]${NC} Grading $STUDENT_ID..."

    # Check if submission exists
    if [ ! -d "$STUDENT_DIR" ]; then
        echo -e "${YELLOW}  ⚠ No submission found${NC}"
        # Add zero-score entry to CSV
        echo "Homework 2 - Events;Homework;$STUDENT_ID;0;0;0;0;0;0;0;20;0;0;0.00;\"No submission found\"" >> "$FINAL_CSV"
        ((STUDENTS_FAILED++))
        continue
    fi

    # Check if eventmanagement package exists
    if [ ! -d "$STUDENT_DIR/eventmanagement" ]; then
        echo -e "${YELLOW}  ⚠ Package structure incorrect${NC}"
        echo "Homework 2 - Events;Homework;$STUDENT_ID;0;0;0;0;0;0;0;20;0;0;0.00;\"Missing eventmanagement package\"" >> "$FINAL_CSV"
        ((STUDENTS_FAILED++))
        continue
    fi

    # Run auto-grader
    java EventSystemAutoGrader "$STUDENT_DIR" "$STUDENT_CSV" > "$RESULTS_DIR/${STUDENT_ID}_log.txt" 2>&1

    if [ $? -eq 0 ] && [ -f "$STUDENT_CSV" ]; then
        # Append to final CSV (skip header)
        tail -n +1 "$STUDENT_CSV" >> "$FINAL_CSV"
        echo -e "${GREEN}  ✓ Graded successfully${NC}"
        ((STUDENTS_GRADED++))

        # Check for defense requirement
        if grep -q "DEFENSE REQUIRED" "$STUDENT_CSV"; then
            echo -e "${YELLOW}  ⚠ Defense required${NC}"
            # Extract reason from CSV
            REASON=$(grep "DEFENSE REQUIRED" "$STUDENT_CSV" | sed 's/.*DEFENSE REQUIRED: \(.*\)"/\1/')
            echo "$STUDENT_ID - $REASON" >> "$DEFENSE_LIST"
        fi
    else
        echo -e "${RED}  ✗ Grading failed${NC}"
        echo "Homework 2 - Events;Homework;$STUDENT_ID;0;0;0;0;0;0;0;20;0;0;0.00;\"Grading failed - see log\"" >> "$FINAL_CSV"
        ((STUDENTS_FAILED++))
    fi

    echo ""
done

# ==============================================================================
# Summary Statistics
# ==============================================================================

echo -e "${BLUE}===============================================${NC}"
echo -e "${BLUE}   Grading Summary${NC}"
echo -e "${BLUE}===============================================${NC}"
echo ""
echo -e "Total students:        ${BLUE}$TOTAL_STUDENTS${NC}"
echo -e "Successfully graded:   ${GREEN}$STUDENTS_GRADED${NC}"
echo -e "Failed/Missing:        ${RED}$STUDENTS_FAILED${NC}"
echo ""

# Count defense requirements
if [ -f "$DEFENSE_LIST" ]; then
    DEFENSE_COUNT=$(wc -l < "$DEFENSE_LIST")
    echo -e "Defense required:      ${YELLOW}$DEFENSE_COUNT${NC}"
else
    DEFENSE_COUNT=0
    echo -e "Defense required:      ${GREEN}0${NC}"
fi

echo ""

# Calculate statistics from CSV
if [ -f "$FINAL_CSV" ]; then
    echo -e "${BLUE}Grade Distribution:${NC}"

    # Count grades (based on percentage)
    GRADE_6=$(awk -F';' 'NR>1 {gsub(/\./, ",", $14); if ($14 >= 90) count++} END {print count+0}' "$FINAL_CSV")
    GRADE_5=$(awk -F';' 'NR>1 {gsub(/\./, ",", $14); if ($14 >= 75 && $14 < 90) count++} END {print count+0}' "$FINAL_CSV")
    GRADE_4=$(awk -F';' 'NR>1 {gsub(/\./, ",", $14); if ($14 >= 65 && $14 < 75) count++} END {print count+0}' "$FINAL_CSV")
    GRADE_3=$(awk -F';' 'NR>1 {gsub(/\./, ",", $14); if ($14 >= 55 && $14 < 65) count++} END {print count+0}' "$FINAL_CSV")
    GRADE_2=$(awk -F';' 'NR>1 {gsub(/\./, ",", $14); if ($14 < 55) count++} END {print count+0}' "$FINAL_CSV")

    echo "  Grade 6 (90%+):   $GRADE_6 students"
    echo "  Grade 5 (75-89%): $GRADE_5 students"
    echo "  Grade 4 (65-74%): $GRADE_4 students"
    echo "  Grade 3 (55-64%): $GRADE_3 students"
    echo "  Grade 2 (<55%):   $GRADE_2 students"
    echo ""

    # Calculate average
    AVG_PERCENTAGE=$(awk -F';' 'NR>1 {sum+=$14; count++} END {if (count>0) printf "%.1f", sum/count; else print "0"}' "$FINAL_CSV")
    echo -e "Average score:         ${BLUE}$AVG_PERCENTAGE%${NC}"
fi

echo ""

# ==============================================================================
# Output Files
# ==============================================================================

echo -e "${BLUE}Output Files:${NC}"
echo -e "  📄 Final CSV:        ${GREEN}$FINAL_CSV${NC}"

if [ -f "$DEFENSE_LIST" ] && [ $DEFENSE_COUNT -gt 0 ]; then
    echo -e "  📋 Defense List:     ${YELLOW}$DEFENSE_LIST${NC}"
fi

echo -e "  📁 Individual logs:  ${BLUE}$RESULTS_DIR/S_*_log.txt${NC}"
echo ""

# ==============================================================================
# Defense List Details
# ==============================================================================

if [ -f "$DEFENSE_LIST" ] && [ $DEFENSE_COUNT -gt 0 ]; then
    echo -e "${YELLOW}===============================================${NC}"
    echo -e "${YELLOW}   Students Requiring Defense Interview${NC}"
    echo -e "${YELLOW}===============================================${NC}"
    echo ""
    cat "$DEFENSE_LIST"
    echo ""
    echo -e "${YELLOW}Please schedule defense interviews for these students.${NC}"
    echo -e "${YELLOW}Suggested questions available in RUBRIC.md${NC}"
    echo ""
fi

# ==============================================================================
# Import Instructions
# ==============================================================================

echo -e "${GREEN}===============================================${NC}"
echo -e "${GREEN}   Next Steps${NC}"
echo -e "${GREEN}===============================================${NC}"
echo ""
echo "1. Review the final CSV file:"
echo "   $FINAL_CSV"
echo ""
echo "2. Import CSV into Student Performance Analytics Dashboard:"
echo "   - Open student-performance-analytics-v5.0.html"
echo "   - Click '📁 Качи CSV' button"
echo "   - Select $FINAL_CSV"
echo ""

if [ $DEFENSE_COUNT -gt 0 ]; then
    echo "3. Schedule defense interviews for $DEFENSE_COUNT students:"
    echo "   - See $DEFENSE_LIST for details"
    echo "   - Use questions from RUBRIC.md section 'Defense Interview'"
    echo ""
    echo "4. Update scores after defense interviews"
    echo "   - Modify CSV manually for students who fail defense"
    echo "   - Re-import updated CSV to dashboard"
    echo ""
fi

echo -e "${GREEN}✓ Batch grading complete!${NC}"
echo ""
