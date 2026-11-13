# -*- coding: utf-8 -*-
"""
Gera o Documento Consolidado do Projeto FinApp em PDF.

Junta os documentos técnicos e de gestão das Rodadas 1, 2 e 3 (Markdown em docs/),
acrescenta os gráficos vetoriais (EAP, Gantt, Curva S e burndowns) e imprime o
resultado em PDF com um navegador baseado em Chromium em modo headless
(Google Chrome, Microsoft Edge, Chromium ou Brave) no macOS, no Windows ou no Linux.

Uso (Python 3.10 ou superior):
    python3 -m pip install markdown
    python3 build_consolidated_pdf.py
    python3 build_consolidated_pdf.py --data 13/11/2026
    python3 build_consolidated_pdf.py --navegador "/caminho/do/navegador"

No Windows, use "py" no lugar de "python3". O navegador também pode ser indicado
pela variável de ambiente PDF_BROWSER.

Saídas: FinApp_Documento_Consolidado_Final.pdf (na raiz e em docs/) e o HTML
intermediário Documento_Consolidado_FinApp.html (ignorado pelo Git), útil para
conferir o layout no navegador antes de gerar o PDF.
"""

import argparse
import datetime
import os
import platform
import posixpath
import re
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path

try:
    import markdown
except ImportError:
    sys.exit("Falta o pacote 'markdown'. Instale com: python3 -m pip install markdown")

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
DOCS_DIR = os.path.join(BASE_DIR, "docs")
OUTPUT_HTML = os.path.join(BASE_DIR, "Documento_Consolidado_FinApp.html")
OUTPUT_PDF = os.path.join(BASE_DIR, "FinApp_Documento_Consolidado_Final.pdf")
OUTPUT_PDF_DOCS = os.path.join(DOCS_DIR, "FinApp_Documento_Consolidado_Final.pdf")
REPO_URL = "https://github.com/ArbenTafili/finapp_Uff"

def read_doc(rel_path):
    full_path = os.path.join(DOCS_DIR, rel_path)
    if not os.path.exists(full_path):
        print(f"Aviso: docs/{rel_path} não encontrado; o capítulo ficará sem esse texto.")
        return ""
    with open(full_path, "r", encoding="utf-8") as f:
        return f.read()


# Geradores de SVG de alta precisao e visual moderno

def svg_eap_diagram():
    return """
    <div class="diagram-box">
      <div class="diagram-title">Estrutura Analitica do Projeto (EAP / WBS) - Diagrama Hierarquico</div>
      <svg viewBox="0 0 1100 560" width="100%" height="auto" xmlns="http://www.w3.org/2000/svg">
        <defs>
          <linearGradient id="gradRoot" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#1e3a8a" />
            <stop offset="100%" stop-color="#2563eb" />
          </linearGradient>
          <linearGradient id="gradN1" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#047857" />
            <stop offset="100%" stop-color="#10b981" />
          </linearGradient>
          <linearGradient id="gradN2" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#b45309" />
            <stop offset="100%" stop-color="#f59e0b" />
          </linearGradient>
          <linearGradient id="gradN3" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#4338ca" />
            <stop offset="100%" stop-color="#6366f1" />
          </linearGradient>
          <linearGradient id="gradN4" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#be185d" />
            <stop offset="100%" stop-color="#ec4899" />
          </linearGradient>
          <linearGradient id="gradN5" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#0f766e" />
            <stop offset="100%" stop-color="#14b8a6" />
          </linearGradient>
          <filter id="shadow" x="-5%" y="-5%" width="110%" height="110%">
            <feDropShadow dx="0" dy="3" stdDeviation="3" flood-opacity="0.15" />
          </filter>
        </defs>

        <!-- Conectores principais -->
        <path d="M 550,55 L 550,85" stroke="#94a3b8" stroke-width="2.5" fill="none" />
        <path d="M 110,85 L 990,85" stroke="#94a3b8" stroke-width="2.5" fill="none" />
        <path d="M 110,85 L 110,115" stroke="#94a3b8" stroke-width="2.5" fill="none" />
        <path d="M 330,85 L 330,115" stroke="#94a3b8" stroke-width="2.5" fill="none" />
        <path d="M 550,85 L 550,115" stroke="#94a3b8" stroke-width="2.5" fill="none" />
        <path d="M 770,85 L 770,115" stroke="#94a3b8" stroke-width="2.5" fill="none" />
        <path d="M 990,85 L 990,115" stroke="#94a3b8" stroke-width="2.5" fill="none" />

        <!-- No Raiz -->
        <rect x="420" y="10" width="260" height="45" rx="8" fill="url(#gradRoot)" filter="url(#shadow)" />
        <text x="550" y="38" text-anchor="middle" font-family="'Segoe UI', Roboto, sans-serif" font-weight="700" font-size="16" fill="#ffffff">Projeto FinApp (Finanças)</text>

        <!-- Nivel 2: Macroetapas -->
        <!-- 1. Governanca -->
        <rect x="20" y="115" width="180" height="40" rx="6" fill="url(#gradN1)" filter="url(#shadow)" />
        <text x="110" y="140" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-weight="600" font-size="13" fill="#ffffff">1.0 Governança &amp; Setup</text>

        <!-- 2. Arquitetura -->
        <rect x="240" y="115" width="180" height="40" rx="6" fill="url(#gradN2)" filter="url(#shadow)" />
        <text x="330" y="140" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-weight="600" font-size="13" fill="#ffffff">2.0 Arquitetura &amp; Infra</text>

        <!-- 3. Requisitos Funcionais -->
        <rect x="460" y="115" width="180" height="40" rx="6" fill="url(#gradN3)" filter="url(#shadow)" />
        <text x="550" y="140" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-weight="600" font-size="13" fill="#ffffff">3.0 Requisitos Funcionais</text>

        <!-- 4. Qualidade -->
        <rect x="680" y="115" width="180" height="40" rx="6" fill="url(#gradN4)" filter="url(#shadow)" />
        <text x="770" y="140" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-weight="600" font-size="13" fill="#ffffff">4.0 Qualidade &amp; Testes</text>

        <!-- 5. Encerramento -->
        <rect x="900" y="115" width="180" height="40" rx="6" fill="url(#gradN5)" filter="url(#shadow)" />
        <text x="990" y="140" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-weight="600" font-size="13" fill="#ffffff">5.0 Encerramento</text>

        <!-- Linhas e Folhas: Nivel 3 -->
        <!-- Coluna 1: Governanca -->
        <line x1="110" y1="155" x2="110" y2="480" stroke="#cbd5e1" stroke-width="2" />
        <g transform="translate(25, 175)" font-family="'Segoe UI', sans-serif" font-size="11" fill="#1e293b">
          <rect x="0" y="0" width="170" height="35" rx="4" fill="#ecfdf5" stroke="#a7f3d0" />
          <text x="85" y="22" text-anchor="middle">1.1 Repositório e CI/CD</text>
          <rect x="0" y="45" width="170" height="35" rx="4" fill="#ecfdf5" stroke="#a7f3d0" />
          <text x="85" y="67" text-anchor="middle">1.2 Backlog &amp; User Stories</text>
          <rect x="0" y="90" width="170" height="35" rx="4" fill="#ecfdf5" stroke="#a7f3d0" />
          <text x="85" y="112" text-anchor="middle">1.3 Planejamento &amp; Poker</text>
          <rect x="0" y="135" width="170" height="35" rx="4" fill="#ecfdf5" stroke="#a7f3d0" />
          <text x="85" y="157" text-anchor="middle">1.4 Modelagem de Classes</text>
          <rect x="0" y="180" width="170" height="35" rx="4" fill="#ecfdf5" stroke="#a7f3d0" />
          <text x="85" y="202" text-anchor="middle">1.5 Regras e Datas (ADR)</text>
          <rect x="0" y="225" width="170" height="35" rx="4" fill="#ecfdf5" stroke="#a7f3d0" />
          <text x="85" y="247" text-anchor="middle">1.6 Baseline de GP e EVM</text>
        </g>

        <!-- Coluna 2: Arquitetura -->
        <line x1="330" y1="155" x2="330" y2="440" stroke="#cbd5e1" stroke-width="2" />
        <g transform="translate(245, 175)" font-family="'Segoe UI', sans-serif" font-size="11" fill="#1e293b">
          <rect x="0" y="0" width="170" height="35" rx="4" fill="#fffbeb" stroke="#fde68a" />
          <text x="85" y="22" text-anchor="middle">2.1 Spring Boot / Kotlin</text>
          <rect x="0" y="45" width="170" height="35" rx="4" fill="#fffbeb" stroke="#fde68a" />
          <text x="85" y="67" text-anchor="middle">2.2 Docker &amp; Compose</text>
          <rect x="0" y="90" width="170" height="35" rx="4" fill="#fffbeb" stroke="#fde68a" />
          <text x="85" y="112" text-anchor="middle">2.3 PostgreSQL &amp; Flyway</text>
          <rect x="0" y="135" width="170" height="35" rx="4" fill="#fffbeb" stroke="#fde68a" />
          <text x="85" y="157" text-anchor="middle">2.4 Migração RDT-01</text>
          <rect x="0" y="180" width="170" height="35" rx="4" fill="#fffbeb" stroke="#fde68a" />
          <text x="85" y="202" text-anchor="middle">2.5 Deploy Cloud Render</text>
        </g>

        <!-- Coluna 3: Requisitos Funcionais -->
        <line x1="550" y1="155" x2="550" y2="440" stroke="#cbd5e1" stroke-width="2" />
        <g transform="translate(465, 175)" font-family="'Segoe UI', sans-serif" font-size="11" fill="#1e293b">
          <rect x="0" y="0" width="170" height="35" rx="4" fill="#eef2ff" stroke="#c7d2fe" />
          <text x="85" y="22" text-anchor="middle" font-weight="600">3.1 RF01 Transações</text>
          <rect x="0" y="45" width="170" height="35" rx="4" fill="#eef2ff" stroke="#c7d2fe" />
          <text x="85" y="67" text-anchor="middle" font-weight="600">3.2 RF02 Categorias</text>
          <rect x="0" y="90" width="170" height="35" rx="4" fill="#eef2ff" stroke="#c7d2fe" />
          <text x="85" y="112" text-anchor="middle" font-weight="600">3.3 RF03 Relatórios</text>
          <rect x="0" y="135" width="170" height="35" rx="4" fill="#eef2ff" stroke="#c7d2fe" />
          <text x="85" y="157" text-anchor="middle" font-weight="600">3.4 RF04 Metas Economia</text>
          <rect x="0" y="180" width="170" height="35" rx="4" fill="#eef2ff" stroke="#c7d2fe" />
          <text x="85" y="202" text-anchor="middle" font-weight="600">3.5 RF05 Exportação CSV</text>
        </g>

        <!-- Coluna 4: Qualidade -->
        <line x1="770" y1="155" x2="770" y2="440" stroke="#cbd5e1" stroke-width="2" />
        <g transform="translate(685, 175)" font-family="'Segoe UI', sans-serif" font-size="11" fill="#1e293b">
          <rect x="0" y="0" width="170" height="35" rx="4" fill="#fdf2f8" stroke="#fbcfe8" />
          <text x="85" y="22" text-anchor="middle">4.1 109 Testes Automat.</text>
          <rect x="0" y="45" width="170" height="35" rx="4" fill="#fdf2f8" stroke="#fbcfe8" />
          <text x="85" y="67" text-anchor="middle">4.2 Integração Testcontainers</text>
          <rect x="0" y="90" width="170" height="35" rx="4" fill="#fdf2f8" stroke="#fbcfe8" />
          <text x="85" y="112" text-anchor="middle">4.3 RNF02 Desempenho &lt; 3s</text>
          <rect x="0" y="135" width="170" height="35" rx="4" fill="#fdf2f8" stroke="#fbcfe8" />
          <text x="85" y="157" text-anchor="middle">4.4 RNF04 Cross-Browser</text>
          <rect x="0" y="180" width="170" height="35" rx="4" fill="#fdf2f8" stroke="#fbcfe8" />
          <text x="85" y="202" text-anchor="middle">4.5 RNF05 Precisão Moeda</text>
        </g>

        <!-- Coluna 5: Encerramento -->
        <line x1="990" y1="155" x2="990" y2="390" stroke="#cbd5e1" stroke-width="2" />
        <g transform="translate(905, 175)" font-family="'Segoe UI', sans-serif" font-size="11" fill="#1e293b">
          <rect x="0" y="0" width="170" height="35" rx="4" fill="#f0fdfa" stroke="#99f6e4" />
          <text x="85" y="22" text-anchor="middle">5.1 Demos v1, v2 e v3</text>
          <rect x="0" y="45" width="170" height="35" rx="4" fill="#f0fdfa" stroke="#99f6e4" />
          <text x="85" y="67" text-anchor="middle">5.2 Retrospectiva em Vídeo</text>
          <rect x="0" y="90" width="170" height="35" rx="4" fill="#f0fdfa" stroke="#99f6e4" />
          <text x="85" y="112" text-anchor="middle">5.3 Documento Consolidado</text>
        </g>
      </svg>
    </div>
    """

def svg_gantt_diagram():
    return """
    <div class="diagram-box">
      <div class="diagram-title">Cronograma Detalhado (Gantt) — Rastreamento de Prazo e Atrasos (Rodadas 1 a 3)</div>
      <svg viewBox="0 0 1100 620" width="100%" height="auto" xmlns="http://www.w3.org/2000/svg">
        <defs>
          <filter id="ganttShadow" x="-2%" y="-2%" width="104%" height="104%">
            <feDropShadow dx="0" dy="2" stdDeviation="2" flood-opacity="0.1" />
          </filter>
        </defs>

        <!-- Fundo e Grade de Datas -->
        <rect x="260" y="30" width="810" height="570" fill="#f8fafc" stroke="#e2e8f0" />
        
        <!-- Marcadores de Dias / Semanas -->
        <!-- 01/09 (x=260) a 28/10 (x=1070) -> 58 dias total = ~14px por dia -->
        <!-- R1: 01/09 a 14/09 (dias 0 a 13 -> x=260 a x=442) -->
        <!-- R2: 15/09 a 06/10 (dias 14 a 35 -> x=456 a x=750) -->
        <!-- R3: 07/10 a 28/10 (dias 36 a 57 -> x=764 a x=1058) -->

        <!-- Faixas verticais das rodadas -->
        <rect x="260" y="30" width="190" height="570" fill="#f0fdf4" opacity="0.6" />
        <rect x="450" y="30" width="300" height="570" fill="#fffbeb" opacity="0.6" />
        <rect x="750" y="30" width="320" height="570" fill="#eff6ff" opacity="0.6" />

        <!-- Cabecalho de datas -->
        <text x="355" y="22" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-weight="700" font-size="12" fill="#15803d">RODADA 1 (01/09 a 14/09)</text>
        <text x="600" y="22" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-weight="700" font-size="12" fill="#b45309">RODADA 2 (15/09 a 06/10)</text>
        <text x="910" y="22" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-weight="700" font-size="12" fill="#1d4ed8">RODADA 3 (07/10 a 28/10)</text>

        <!-- Linhas e Tarefas -->
        <!-- Funcao helper de linha: y_pos, label, x, w, fill, status_label, status_color -->
        
        <!-- Grupo R1 -->
        <g font-family="'Segoe UI', sans-serif" font-size="11">
          <!-- T1 -->
          <text x="10" y="55" font-weight="600" fill="#334155">1.1 Setup Repo e CI/CD</text>
          <rect x="260" y="42" width="70" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="340" y="55" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T2 -->
          <text x="10" y="80" font-weight="600" fill="#334155">1.2 Backlog &amp; User Stories</text>
          <rect x="260" y="67" width="85" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="355" y="80" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T3 -->
          <text x="10" y="105" font-weight="600" fill="#334155">1.4 Modelagem de Classes</text>
          <rect x="330" y="92" width="100" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="440" y="105" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T4 -->
          <text x="10" y="130" font-weight="600" fill="#334155">1.5 Regras e Datas Validadas</text>
          <rect x="345" y="117" width="100" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="455" y="130" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- M1 -->
          <text x="10" y="155" font-weight="700" fill="#0f766e">★ Demo v1 Homologada (14/09)</text>
          <polygon points="445,142 453,150 445,158 437,150" fill="#0d9488" />
          <line x1="445" y1="42" x2="445" y2="165" stroke="#0d9488" stroke-width="1.5" stroke-dasharray="3,3" />

          <!-- Separador R2 -->
          <line x1="0" y1="172" x2="1070" y2="172" stroke="#e2e8f0" stroke-width="1" />

          <!-- T5 -->
          <text x="10" y="195" font-weight="600" fill="#334155">3.1 Conclusão RF01 Transações</text>
          <rect x="455" y="182" width="130" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="595" y="195" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T6 -->
          <text x="10" y="220" font-weight="600" fill="#334155">3.2 Conclusão RF02 Categorias</text>
          <rect x="525" y="207" width="115" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="650" y="220" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T7 - ATRASO RDT-01 -->
          <text x="10" y="245" font-weight="700" fill="#b91c1c">2.2 Docker &amp; Postgres (RDT-01)</text>
          <rect x="495" y="232" width="160" height="18" rx="3" fill="#ef4444" filter="url(#ganttShadow)" />
          <text x="665" y="245" font-size="10" fill="#b91c1c" font-weight="700">Atraso RDT-01 (+8h)</text>

          <!-- T8 - ADIANTADO -->
          <text x="10" y="270" font-weight="700" fill="#15803d">3.3 Queries Agregadas RF03</text>
          <rect x="625" y="257" width="100" height="18" rx="3" fill="#3b82f6" filter="url(#ganttShadow)" />
          <text x="735" y="270" font-size="10" fill="#1d4ed8" font-weight="700">Adiantado (-2d)</text>

          <!-- T9 -->
          <text x="10" y="295" font-weight="600" fill="#334155">Governança Branches &amp; EVM R2</text>
          <rect x="610" y="282" width="125" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="745" y="295" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- M2 -->
          <text x="10" y="320" font-weight="700" fill="#b45309">★ Demo v2 Homologada (06/10)</text>
          <polygon points="745,307 753,315 745,323 737,315" fill="#f59e0b" />
          <line x1="745" y1="182" x2="745" y2="330" stroke="#f59e0b" stroke-width="1.5" stroke-dasharray="3,3" />

          <!-- Separador R3 -->
          <line x1="0" y1="337" x2="1070" y2="337" stroke="#e2e8f0" stroke-width="1" />

          <!-- T10 -->
          <text x="10" y="360" font-weight="600" fill="#334155">3.3 Interface Relatórios Web</text>
          <rect x="755" y="347" width="130" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="895" y="360" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T11 -->
          <text x="10" y="385" font-weight="600" fill="#334155">3.4 Metas de Economia RF04</text>
          <rect x="795" y="372" width="165" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="970" y="385" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T12 -->
          <text x="10" y="410" font-weight="600" fill="#334155">3.5 Exportação CSV RF05</text>
          <rect x="850" y="397" width="125" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="985" y="410" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T13 -->
          <text x="10" y="435" font-weight="600" fill="#334155">3.2 Gestão de Categorias na UI</text>
          <rect x="825" y="422" width="115" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="950" y="435" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T14 -->
          <text x="10" y="460" font-weight="600" fill="#334155">4.9 Deploy Render &amp; Conta Teste</text>
          <rect x="910" y="447" width="100" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="1020" y="460" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T15 -->
          <text x="10" y="485" font-weight="600" fill="#334155">4.1 Suíte 109 Testes Automat.</text>
          <rect x="935" y="472" width="85" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="1030" y="485" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T16 -->
          <text x="10" y="510" font-weight="600" fill="#334155">EVM Consolidado &amp; PDF Master</text>
          <rect x="990" y="497" width="60" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />
          <text x="990" y="527" font-size="10" fill="#047857" font-weight="700">No Prazo</text>

          <!-- T17 -->
          <text x="10" y="535" font-weight="600" fill="#334155">5.2 Retrospectiva Final Gravada</text>
          <rect x="1010" y="522" width="45" height="18" rx="3" fill="#10b981" filter="url(#ganttShadow)" />

          <!-- M3 -->
          <text x="10" y="565" font-weight="700" fill="#1d4ed8">★ Entrega Final &amp; Demo v3 (28/10)</text>
          <polygon points="1055,552 1063,560 1055,568 1047,560" fill="#2563eb" />
          <line x1="1055" y1="347" x2="1055" y2="575" stroke="#2563eb" stroke-width="2" stroke-dasharray="3,3" />
        </g>

        <!-- Legenda -->
        <g transform="translate(10, 585)" font-family="'Segoe UI', sans-serif" font-size="11">
          <rect x="0" y="0" width="15" height="12" rx="2" fill="#10b981" />
          <text x="22" y="10" fill="#334155">Entregue no Prazo</text>

          <rect x="150" y="0" width="15" height="12" rx="2" fill="#ef4444" />
          <text x="172" y="10" fill="#334155">Atraso RDT-01 (+8h / Absorvido)</text>

          <rect x="390" y="0" width="15" height="12" rx="2" fill="#3b82f6" />
          <text x="412" y="10" fill="#334155">Entrega Adiantada</text>

          <polygon points="560,0 568,6 560,12 552,6" fill="#f59e0b" />
          <text x="575" y="10" fill="#334155">Marco / Milestone Homologado</text>
        </g>
      </svg>
    </div>
    """

def svg_curva_s():
    return """
    <div class="diagram-box">
      <div class="diagram-title">Curva S — Valor Agregado Acumulado (PV vs. EV vs. AC ao Longo do Projeto)</div>
      <svg viewBox="0 0 950 420" width="100%" height="auto" xmlns="http://www.w3.org/2000/svg">
        <!-- Grade -->
        <rect x="80" y="30" width="820" height="320" fill="#fafafa" stroke="#e2e8f0" />
        <line x1="80" y1="110" x2="900" y2="110" stroke="#e2e8f0" stroke-dasharray="2,2" />
        <line x1="80" y1="190" x2="900" y2="190" stroke="#e2e8f0" stroke-dasharray="2,2" />
        <line x1="80" y1="270" x2="900" y2="270" stroke="#e2e8f0" stroke-dasharray="2,2" />

        <!-- Eixos -->
        <!-- Y: 0 a 700 h -> h=320 -> y = 350 - (val / 700 * 320) -->
        <text x="70" y="355" text-anchor="end" font-family="'Segoe UI', sans-serif" font-size="11" fill="#64748b">0 h</text>
        <text x="70" y="275" text-anchor="end" font-family="'Segoe UI', sans-serif" font-size="11" fill="#64748b">175 h</text>
        <text x="70" y="195" text-anchor="end" font-family="'Segoe UI', sans-serif" font-size="11" fill="#64748b">350 h</text>
        <text x="70" y="115" text-anchor="end" font-family="'Segoe UI', sans-serif" font-size="11" fill="#64748b">525 h</text>
        <text x="70" y="40" text-anchor="end" font-family="'Segoe UI', sans-serif" font-size="11" fill="#64748b">700 h</text>

        <!-- Marcos no Eixo X -->
        <!-- P0: x=80 (01/09) -->
        <!-- P1: x=244 (14/09 R1) -->
        <!-- P2: x=408 (25/09 R2-Meio) -->
        <!-- P3: x=572 (06/10 R2-Fim) -->
        <!-- P4: x=736 (17/10 R3-Meio) -->
        <!-- P5: x=900 (28/10 R3-Fim) -->
        <text x="80" y="375" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-size="11" fill="#475569">01/09<br/>Início</text>
        <text x="244" y="375" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-size="11" fill="#475569">14/09<br/>Fim R1</text>
        <text x="408" y="375" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-size="11" fill="#475569">25/09<br/>Meio R2</text>
        <text x="572" y="375" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-size="11" fill="#475569">06/10<br/>Fim R2</text>
        <text x="736" y="375" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-size="11" fill="#475569">17/10<br/>Meio R3</text>
        <text x="900" y="375" text-anchor="middle" font-family="'Segoe UI', sans-serif" font-weight="700" font-size="11" fill="#1e3a8a">28/10<br/>Entrega Final</text>

        <!-- Linhas Curva S -->
        <!-- PV: [0, 120, 210, 300, 456, 612] -->
        <!-- y = 350 - val * (320/700) = 350 - val * 0.4571 -->
        <!-- 0->350 | 120->295 | 210->254 | 300->213 | 456->142 | 612->70 -->
        <polyline points="80,350 244,295 408,254 572,213 736,142 900,70" fill="none" stroke="#2563eb" stroke-width="3" stroke-dasharray="6,4" />

        <!-- EV: [0, 120, 205, 304, 465, 612] -->
        <!-- 0->350 | 120->295 | 205->256 | 304->211 | 465->137 | 612->70 -->
        <polyline points="80,350 244,295 408,256 572,211 736,137 900,70" fill="none" stroke="#16a34a" stroke-width="3.5" />

        <!-- AC: [0, 120, 218, 310, 470, 615] -->
        <!-- 0->350 | 120->295 | 218->250 | 310->208 | 470->135 | 615->69 -->
        <polyline points="80,350 244,295 408,250 572,208 736,135 900,69" fill="none" stroke="#dc2626" stroke-width="3" />

        <!-- Pontos nos marcos finais (x=900) -->
        <circle cx="900" cy="70" r="6" fill="#16a34a" />
        <circle cx="900" cy="69" r="4" fill="#dc2626" />
        <text x="890" y="55" font-family="'Segoe UI', sans-serif" font-weight="700" font-size="11" fill="#1e293b" text-anchor="end">EV: 612h (100%) | AC: 615h</text>

        <!-- Ponto do desvio na R2 (x=572) -->
        <circle cx="572" cy="208" r="5" fill="#dc2626" />
        <text x="560" y="195" font-family="'Segoe UI', sans-serif" font-size="10" fill="#b91c1c" text-anchor="end">Desvio Docker (AC > EV)</text>

        <!-- Legenda -->
        <g transform="translate(150, 400)" font-family="'Segoe UI', sans-serif" font-size="12">
          <line x1="0" y1="8" x2="30" y2="8" stroke="#2563eb" stroke-width="3" stroke-dasharray="6,4" />
          <text x="38" y="12" fill="#1e293b">PV (Valor Planejado - Baseline)</text>

          <line x1="260" y1="8" x2="290" y2="8" stroke="#16a34a" stroke-width="3.5" />
          <text x="298" y="12" fill="#1e293b">EV (Valor Agregado Entregue)</text>

          <line x1="520" y1="8" x2="550" y2="8" stroke="#dc2626" stroke-width="3" />
          <text x="558" y="12" fill="#1e293b">AC (Custo Real Incorrido)</text>
        </g>
      </svg>
    </div>
    """

def svg_burndowns():
    return """
    <div class="diagram-box">
      <div class="diagram-title">Gráficos de Burndown — Visão por Rodada e Consolidado Global</div>
      <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-top: 15px;">
        
        <!-- Burndown R1 -->
        <div style="background: #fafafa; border: 1px solid #e2e8f0; border-radius: 6px; padding: 10px;">
          <div style="font-weight: 700; font-size: 13px; color: #166534; text-align: center; margin-bottom: 5px;">Burndown Rodada 1 (120h)</div>
          <svg viewBox="0 0 400 200" width="100%" height="auto">
            <line x1="50" y1="20" x2="50" y2="160" stroke="#cbd5e1" />
            <line x1="50" y1="160" x2="380" y2="160" stroke="#cbd5e1" />
            <text x="45" y="25" text-anchor="end" font-size="10" fill="#64748b">120h</text>
            <text x="45" y="160" text-anchor="end" font-size="10" fill="#64748b">0h</text>
            <!-- Eixo X: 01/09, 03/09, 06/09, 10/09, 14/09 -->
            <!-- x: 50, 132, 215, 297, 380 -->
            <!-- Planejado: [120, 108, 96, 84, 0] -> y = 160 - (val/120 * 140) -->
            <!-- y: 20, 34, 48, 62, 160 -->
            <polyline points="50,20 132,34 215,48 297,62 380,160" fill="none" stroke="#94a3b8" stroke-width="2" stroke-dasharray="4,4" />
            <!-- Real: [120, 114, 98, 80, 0] -->
            <!-- y: 20, 27, 46, 67, 160 -->
            <polyline points="50,20 132,27 215,46 297,67 380,160" fill="none" stroke="#16a34a" stroke-width="2.5" />
            <circle cx="380" cy="160" r="4" fill="#16a34a" />
            <text x="50" y="175" text-anchor="middle" font-size="9" fill="#475569">01/09</text>
            <text x="215" y="175" text-anchor="middle" font-size="9" fill="#475569">06/09</text>
            <text x="380" y="175" text-anchor="middle" font-size="9" fill="#475569">14/09</text>
          </svg>
        </div>

        <!-- Burndown R2 -->
        <div style="background: #fafafa; border: 1px solid #e2e8f0; border-radius: 6px; padding: 10px;">
          <div style="font-weight: 700; font-size: 13px; color: #b45309; text-align: center; margin-bottom: 5px;">Burndown Rodada 2 (180h / 190h)</div>
          <svg viewBox="0 0 400 200" width="100%" height="auto">
            <line x1="50" y1="20" x2="50" y2="160" stroke="#cbd5e1" />
            <line x1="50" y1="160" x2="380" y2="160" stroke="#cbd5e1" />
            <text x="45" y="25" text-anchor="end" font-size="10" fill="#64748b">180h</text>
            <text x="45" y="160" text-anchor="end" font-size="10" fill="#64748b">0h</text>
            <!-- Planejado: [180, 140, 100, 60, 20, 0] -->
            <!-- y: 20, 51, 82, 113, 144, 160 -->
            <polyline points="50,20 116,51 182,82 248,113 314,144 380,160" fill="none" stroke="#94a3b8" stroke-width="2" stroke-dasharray="4,4" />
            <!-- Real: [180, 148, 105, 58, 18, 0] -->
            <!-- y: 20, 45, 78, 115, 146, 160 -->
            <polyline points="50,20 116,45 182,78 248,115 314,146 380,160" fill="none" stroke="#f59e0b" stroke-width="2.5" />
            <circle cx="380" cy="160" r="4" fill="#f59e0b" />
            <text x="50" y="175" text-anchor="middle" font-size="9" fill="#475569">15/09</text>
            <text x="182" y="175" text-anchor="middle" font-size="9" fill="#475569">25/09</text>
            <text x="380" y="175" text-anchor="middle" font-size="9" fill="#475569">06/10</text>
          </svg>
        </div>

        <!-- Burndown R3 -->
        <div style="background: #fafafa; border: 1px solid #e2e8f0; border-radius: 6px; padding: 10px;">
          <div style="font-weight: 700; font-size: 13px; color: #1d4ed8; text-align: center; margin-bottom: 5px;">Burndown Rodada 3 (312h / 305h)</div>
          <svg viewBox="0 0 400 200" width="100%" height="auto">
            <line x1="50" y1="20" x2="50" y2="160" stroke="#cbd5e1" />
            <line x1="50" y1="160" x2="380" y2="160" stroke="#cbd5e1" />
            <text x="45" y="25" text-anchor="end" font-size="10" fill="#64748b">312h</text>
            <text x="45" y="160" text-anchor="end" font-size="10" fill="#64748b">0h</text>
            <!-- Planejado: [312, 250, 180, 100, 30, 0] -->
            <!-- y: 20, 48, 79, 115, 146, 160 -->
            <polyline points="50,20 116,48 182,79 248,115 314,146 380,160" fill="none" stroke="#94a3b8" stroke-width="2" stroke-dasharray="4,4" />
            <!-- Real: [312, 248, 175, 95, 25, 0] -->
            <!-- y: 20, 49, 81, 117, 149, 160 -->
            <polyline points="50,20 116,49 182,81 248,117 314,149 380,160" fill="none" stroke="#2563eb" stroke-width="2.5" />
            <circle cx="380" cy="160" r="4" fill="#2563eb" />
            <text x="50" y="175" text-anchor="middle" font-size="9" fill="#475569">07/10</text>
            <text x="182" y="175" text-anchor="middle" font-size="9" fill="#475569">17/10</text>
            <text x="380" y="175" text-anchor="middle" font-size="9" fill="#475569">28/10</text>
          </svg>
        </div>

        <!-- Burndown Consolidado Global -->
        <div style="background: #fafafa; border: 1px solid #e2e8f0; border-radius: 6px; padding: 10px;">
          <div style="font-weight: 700; font-size: 13px; color: #4338ca; text-align: center; margin-bottom: 5px;">Burndown Consolidado Acumulado (612h)</div>
          <svg viewBox="0 0 400 200" width="100%" height="auto">
            <line x1="50" y1="20" x2="50" y2="160" stroke="#cbd5e1" />
            <line x1="50" y1="160" x2="380" y2="160" stroke="#cbd5e1" />
            <text x="45" y="25" text-anchor="end" font-size="10" fill="#64748b">612h</text>
            <text x="45" y="160" text-anchor="end" font-size="10" fill="#64748b">0h</text>
            <!-- Planejado: [612, 492, 412, 312, 180, 0] -->
            <!-- y = 160 - (val/612 * 140) -->
            <!-- y: 20, 47, 66, 89, 119, 160 -->
            <polyline points="50,20 116,47 182,66 248,89 314,119 380,160" fill="none" stroke="#94a3b8" stroke-width="2" stroke-dasharray="4,4" />
            <!-- Real: [612, 492, 417, 308, 175, 0] -->
            <!-- y: 20, 47, 65, 90, 120, 160 -->
            <polyline points="50,20 116,47 182,65 248,90 314,120 380,160" fill="none" stroke="#4f46e5" stroke-width="3" />
            <circle cx="380" cy="160" r="5" fill="#4f46e5" />
            <text x="50" y="175" text-anchor="middle" font-size="9" fill="#475569">01/09</text>
            <text x="182" y="175" text-anchor="middle" font-size="9" fill="#475569">25/09</text>
            <text x="380" y="175" text-anchor="middle" font-size="9" fill="#475569">28/10</text>
          </svg>
        </div>

      </div>
    </div>
    """

# Visao geral das tres entregas (capitulo 1). Os graficos sao desenhados a partir dos dados,
# nao de coordenadas fixas: o burndown geral le as tabelas das secoes 3.1 a 3.3 de
# docs/rodada3/04-monitoramento-consolidado.md, entao acompanha qualquer correcao nesses numeros.

ANO_PROJETO = 2026

# Uma linha por entrega. Fontes: horas de 05-registro-participacao-consolidado.md, SPI/CPI acumulados
# de 04-monitoramento-consolidado.md (secao 1.1), tags de 02-historico-versoes.md e baseline do
# cronograma da Rodada 1 (rodada1/05-cronograma-orcamento.md).
ENTREGAS = [
    {
        "nome": "Rodada 1", "demo": "Demo v1", "cor": "#16a34a", "cor_texto": "#166534",
        "inicio": "01/09", "fim": "14/09", "base_inicio": "01/09", "base_fim": "14/09",
        "horas_prev": 120, "horas_real": 120, "spi": "1,00", "cpi": "1,00", "tags": "v0.1.0",
        "secao_burndown": "### 3.1",
        "entregou": "Visão do produto, escopo e EAP, papéis, estimativas (Planning Poker), cronograma e "
                    "orçamento; CRUD de transações e categorias com interface web simples e Docker.",
        "docs": ["rodada1/01-documento-visao.md", "rodada1/04-estimativas-planning-poker.md",
                 "rodada1/10-decisoes-tecnicas.md"],
    },
    {
        "nome": "Rodada 2", "demo": "Demo v2", "cor": "#d97706", "cor_texto": "#92400e",
        "inicio": "15/09", "fim": "06/10", "base_inicio": "15/09", "base_fim": "13/10",
        "horas_prev": 180, "horas_real": 190, "spi": "1,01", "cpi": "0,98", "tags": "v0.2.0",
        "secao_burndown": "### 3.2",
        "entregou": "RF01 e RF02 concluídos, PostgreSQL com Flyway (RDT-01), relatório mensal na API "
                    "(RF03 parcial), backlog e EAP revisados, análise de riscos atualizada.",
        "docs": ["rodada2/01-backlog.md", "rodada2/02-escopo-eap.md", "rodada2/analise-riscos-atualizada.md",
                 "adr/RDT-01.md"],
    },
    {
        "nome": "Rodada 3", "demo": "Demo v3", "cor": "#2563eb", "cor_texto": "#1e40af",
        "inicio": "07/10", "fim": "28/10", "base_inicio": "14/10", "base_fim": "28/10",
        "horas_prev": 312, "horas_real": 305, "spi": "1,00", "cpi": "0,995", "tags": "v1.0.0 / v1.0.1",
        "secao_burndown": "### 3.3",
        "entregou": "Interface do relatório (RF03), metas de economia (RF04), exportação CSV (RF05), gestão "
                    "de categorias, deploy com conta de teste e tela de login, suíte com 130 testes, EVM e "
                    "documentação consolidados.",
        "docs": ["rodada3/01-roteiro-demo-v3.md", "rodada3/04-monitoramento-consolidado.md",
                 "rodada3/02-historico-versoes.md", "rodada3/05-registro-participacao-consolidado.md"],
    },
]


def _data(ddmm):
    dia, mes = (int(p) for p in ddmm.split("/")[:2])
    return datetime.date(ANO_PROJETO, mes, dia)


def _ddmm(data):
    return data.strftime("%d/%m")


def _tabela_md(texto, titulo):
    """Linhas (dicts) da primeira tabela Markdown depois do titulo indicado."""
    inicio = texto.find(titulo)
    if inicio < 0:
        return []
    linhas = []
    for linha in texto[inicio:].splitlines()[1:]:
        if linha.startswith("#"):
            break
        if linha.strip().startswith("|"):
            linhas.append([c.strip().strip("*") for c in linha.strip().strip("|").split("|")])
        elif linhas:
            break
    if len(linhas) < 3:
        return []
    cabecalho = linhas[0]
    return [dict(zip(cabecalho, valores)) for valores in linhas[2:]]


def _coluna(linha, *trechos):
    """Valor da coluna cujo nome contem todos os trechos (ignora maiusculas)."""
    for nome, valor in linha.items():
        if all(t.lower() in nome.lower() for t in trechos):
            return valor
    raise KeyError(trechos)


def dados_burndown_geral():
    """Horas restantes do projeto inteiro em cada medicao das tres rodadas.

    Restante do projeto = restante da rodada em curso + horas planejadas das rodadas seguintes.
    """
    texto = read_doc("rodada3/04-monitoramento-consolidado.md")
    pontos = []
    for i, entrega in enumerate(ENTREGAS):
        depois = sum(e["horas_prev"] for e in ENTREGAS[i + 1:])
        for linha in _tabela_md(texto, entrega["secao_burndown"]):
            pontos.append((
                _data(_coluna(linha, "data")),
                depois + float(_coluna(linha, "restante", "planejado")),
                depois + float(_coluna(linha, "restante", "real")),
            ))
    # a mesma data pode aparecer no fim de uma rodada e no inicio da outra
    unicos = {}
    for data, plan, real in pontos:
        unicos.setdefault(data, (data, plan, real))
    return sorted(unicos.values())


def svg_burndown_geral():
    pontos = dados_burndown_geral()
    if not pontos:
        print("Aviso: tabelas de burndown não encontradas em 04-monitoramento-consolidado.md")
        return ""
    total = sum(e["horas_prev"] for e in ENTREGAS)
    larg, alt = 900, 420
    esq, dir_, topo, base = 64, 24, 64, 352
    d0, d1 = _data(ENTREGAS[0]["inicio"]), _data(ENTREGAS[-1]["fim"])
    ymax = 100 * (int(total // 100) + 1)

    def x(data):
        return esq + (data - d0).days / (d1 - d0).days * (larg - esq - dir_)

    def y(valor):
        return base - valor / ymax * (base - topo)

    partes = []
    # faixas das rodadas
    for e in ENTREGAS:
        x0, x1 = x(_data(e["inicio"])), x(_data(e["fim"]) + datetime.timedelta(days=1))
        x1 = min(x1, larg - dir_)
        partes.append(f'<rect x="{x0:.1f}" y="{topo}" width="{x1 - x0:.1f}" height="{base - topo}" '
                      f'fill="{e["cor"]}" fill-opacity="0.07" />')
        meio = (x0 + x1) / 2
        partes.append(f'<text x="{meio:.1f}" y="{topo - 30}" text-anchor="middle" font-size="13" '
                      f'font-weight="700" fill="{e["cor_texto"]}">{e["nome"]} · {e["demo"]}</text>')
        partes.append(f'<text x="{meio:.1f}" y="{topo - 13}" text-anchor="middle" font-size="11" fill="#475569">'
                      f'{e["inicio"]} a {e["fim"]} · {e["horas_prev"]} h prev. / {e["horas_real"]} h reais</text>')
    # grade e eixo y
    for v in range(0, ymax + 1, 100):
        partes.append(f'<line x1="{esq}" y1="{y(v):.1f}" x2="{larg - dir_}" y2="{y(v):.1f}" stroke="#e2e8f0" />')
        partes.append(f'<text x="{esq - 8}" y="{y(v) + 4:.1f}" text-anchor="end" font-size="11" fill="#64748b">{v} h</text>')
    partes.append(f'<text x="16" y="{(topo + base) / 2:.1f}" text-anchor="middle" font-size="11" fill="#475569" '
                  f'transform="rotate(-90 16 {(topo + base) / 2:.1f})">Horas restantes do projeto</text>')
    # eixo x: marca em cada medicao; rotulo so onde cabe, com prioridade para inicio e fim das rodadas
    limites = {_data(e[k]) for e in ENTREGAS for k in ("inicio", "fim")}
    rotulados = []
    for data in sorted((d for d, _, _ in pontos), key=lambda d: (d not in limites, d)):
        if all(abs(x(data) - x(outra)) >= 34 for outra in rotulados):
            rotulados.append(data)
    for data, _, _ in pontos:
        partes.append(f'<line x1="{x(data):.1f}" y1="{base}" x2="{x(data):.1f}" y2="{base + 4}" stroke="#94a3b8" />')
        if data in rotulados:
            partes.append(f'<text x="{x(data):.1f}" y="{base + 17}" text-anchor="middle" font-size="10" '
                          f'fill="#64748b">{_ddmm(data)}</text>')
    # linhas planejado e realizado
    planejado = " ".join(f"{x(d):.1f},{y(p):.1f}" for d, p, _ in pontos)
    realizado = " ".join(f"{x(d):.1f},{y(r):.1f}" for d, _, r in pontos)
    partes.append(f'<polyline points="{planejado}" fill="none" stroke="#94a3b8" stroke-width="2" stroke-dasharray="6,5" />')
    partes.append(f'<polyline points="{realizado}" fill="none" stroke="#4f46e5" stroke-width="3" stroke-linejoin="round" />')
    for d, _, r in pontos:
        partes.append(f'<circle cx="{x(d):.1f}" cy="{y(r):.1f}" r="3.2" fill="#4f46e5" />')
    # entregas (marcos)
    for e in ENTREGAS:
        fim = _data(e["fim"])
        real = next(r for d, _, r in pontos if d == fim)
        cx, cy = x(fim), y(real)
        partes.append(f'<rect x="{cx - 7:.1f}" y="{cy - 7:.1f}" width="14" height="14" fill="{e["cor"]}" '
                      f'stroke="#ffffff" stroke-width="2" transform="rotate(45 {cx:.1f} {cy:.1f})" />')
        # ultimo marco fica no canto: rotulo curto a esquerda, rente ao eixo, abaixo da linha
        if fim == d1:
            ancora, dx, dy, rotulo = "end", -30, -6, f'{e["demo"]} · {_ddmm(fim)}'
        else:
            ancora, dx, dy, rotulo = "start", 10, -12, f'{e["demo"]} · {_ddmm(fim)} · {real:.0f} h'
        partes.append(f'<text x="{cx + dx:.1f}" y="{cy + dy:.1f}" text-anchor="{ancora}" font-size="11" '
                      f'font-weight="700" fill="{e["cor_texto"]}">{rotulo}</text>')
    # legenda
    ly = alt - 22
    partes.append(
        f'<g font-size="11" fill="#334155">'
        f'<line x1="{esq}" y1="{ly}" x2="{esq + 28}" y2="{ly}" stroke="#4f46e5" stroke-width="3" />'
        f'<text x="{esq + 36}" y="{ly + 4}">Realizado</text>'
        f'<line x1="{esq + 120}" y1="{ly}" x2="{esq + 148}" y2="{ly}" stroke="#94a3b8" stroke-width="2" stroke-dasharray="6,5" />'
        f'<text x="{esq + 156}" y="{ly + 4}">Planejado</text>'
        f'<rect x="{esq + 246}" y="{ly - 5}" width="10" height="10" fill="#475569" transform="rotate(45 {esq + 251} {ly})" />'
        f'<text x="{esq + 264}" y="{ly + 4}">Entrega (demo)</text>'
        f'<text x="{larg - dir_}" y="{ly + 4}" text-anchor="end" fill="#64748b">'
        f'Restante do projeto = restante da rodada + horas planejadas das rodadas seguintes</text>'
        f'</g>'
    )
    return f"""
    <div class="diagram-box">
      <div class="diagram-title">Burndown Geral do Projeto — as Três Entregas ({total} h planejadas)</div>
      <svg viewBox="0 0 {larg} {alt}" width="100%" height="auto" xmlns="http://www.w3.org/2000/svg"
           font-family="'Segoe UI', -apple-system, 'Helvetica Neue', sans-serif">
        {''.join(partes)}
      </svg>
    </div>
    """


def svg_horas_por_entrega():
    larg, alt = 900, 300
    esq, topo, base = 64, 30, 232
    ymax = 100 * (max(max(e["horas_prev"], e["horas_real"]) for e in ENTREGAS) // 100 + 1)
    grupo = (larg - esq - 24) / len(ENTREGAS)
    barra = 70

    def y(valor):
        return base - valor / ymax * (base - topo)

    partes = []
    for v in range(0, ymax + 1, 50):
        partes.append(f'<line x1="{esq}" y1="{y(v):.1f}" x2="{larg - 24}" y2="{y(v):.1f}" stroke="#e2e8f0" />')
        partes.append(f'<text x="{esq - 8}" y="{y(v) + 4:.1f}" text-anchor="end" font-size="11" fill="#64748b">{v} h</text>')
    for i, e in enumerate(ENTREGAS):
        centro = esq + grupo * (i + 0.5)
        for j, (valor, cor, rotulo) in enumerate(((e["horas_prev"], "#cbd5e1", "prev."),
                                                  (e["horas_real"], e["cor"], "real"))):
            bx = centro - barra - 4 + j * (barra + 8)
            partes.append(f'<rect x="{bx:.1f}" y="{y(valor):.1f}" width="{barra}" height="{base - y(valor):.1f}" '
                          f'rx="3" fill="{cor}" />')
            partes.append(f'<text x="{bx + barra / 2:.1f}" y="{y(valor) - 6:.1f}" text-anchor="middle" font-size="12" '
                          f'font-weight="700" fill="#1e293b">{valor} h</text>')
            partes.append(f'<text x="{bx + barra / 2:.1f}" y="{base + 15}" text-anchor="middle" font-size="10" '
                          f'fill="#64748b">{rotulo}</text>')
        desvio = e["horas_real"] - e["horas_prev"]
        sinal = f"+{desvio}" if desvio > 0 else (f"−{-desvio}" if desvio < 0 else "0")
        partes.append(f'<text x="{centro:.1f}" y="{base + 35}" text-anchor="middle" font-size="13" font-weight="700" '
                      f'fill="{e["cor_texto"]}">{e["nome"]} · {e["demo"]}</text>')
        partes.append(f'<text x="{centro:.1f}" y="{base + 53}" text-anchor="middle" font-size="11" fill="#475569">'
                      f'desvio {sinal} h · SPI {e["spi"]} · CPI {e["cpi"]} (acumulados)</text>')
    return f"""
    <div class="diagram-box">
      <div class="diagram-title">Horas por Entrega — Previstas × Reais</div>
      <svg viewBox="0 0 {larg} {alt}" width="100%" height="auto" xmlns="http://www.w3.org/2000/svg"
           font-family="'Segoe UI', -apple-system, 'Helvetica Neue', sans-serif">
        {''.join(partes)}
      </svg>
    </div>
    """


def svg_linha_do_tempo():
    """Periodo de cada rodada no cronograma da Rodada 1 (baseline) e no realizado."""
    larg, alt = 900, 210
    esq, dir_ = 150, 24
    datas = [_data(e[k]) for e in ENTREGAS for k in ("inicio", "fim", "base_inicio", "base_fim")]
    d0, d1 = min(datas), max(datas)

    def x(data):
        return esq + (data - d0).days / (d1 - d0).days * (larg - esq - dir_)

    faixas = (("Baseline (Rodada 1)", "base_inicio", "base_fim", 40), ("Realizado", "inicio", "fim", 104))
    partes = []
    for rotulo, k_ini, k_fim, ty in faixas:
        partes.append(f'<text x="{esq - 12}" y="{ty + 22}" text-anchor="end" font-size="12" font-weight="700" '
                      f'fill="#334155">{rotulo}</text>')
        for e in ENTREGAS:
            ini, fim = _data(e[k_ini]), _data(e[k_fim])
            x0, x1 = x(ini), x(fim + datetime.timedelta(days=1)) - 2
            x1 = min(x1, larg - dir_)
            # baseline em tom claro com texto escuro; realizado em cor cheia com texto branco
            opaco, texto = ("1", "#ffffff") if k_ini == "inicio" else ("0.3", e["cor_texto"])
            partes.append(f'<rect x="{x0:.1f}" y="{ty}" width="{x1 - x0:.1f}" height="34" rx="4" '
                          f'fill="{e["cor"]}" fill-opacity="{opaco}" />')
            dias = (fim - ini).days + 1
            partes.append(f'<text x="{(x0 + x1) / 2:.1f}" y="{ty + 15}" text-anchor="middle" font-size="11" '
                          f'font-weight="700" fill="{texto}">{e["nome"]}</text>')
            partes.append(f'<text x="{(x0 + x1) / 2:.1f}" y="{ty + 28}" text-anchor="middle" font-size="10" '
                          f'fill="{texto}">{_ddmm(ini)} a {_ddmm(fim)} · {dias} dias</text>')
    # eixo com as datas de fim (entregas)
    marcas = sorted({_data(e[k]) for e in ENTREGAS for k in ("fim", "base_fim")} | {d0})
    for data in marcas:
        partes.append(f'<line x1="{x(data):.1f}" y1="150" x2="{x(data):.1f}" y2="156" stroke="#94a3b8" />')
        partes.append(f'<text x="{x(data):.1f}" y="170" text-anchor="middle" font-size="10" fill="#64748b">{_ddmm(data)}</text>')
    partes.append(f'<line x1="{esq}" y1="150" x2="{larg - dir_}" y2="150" stroke="#cbd5e1" />')
    r2 = ENTREGAS[1]
    antecipou = (_data(r2["base_fim"]) - _data(r2["fim"])).days
    partes.append(f'<text x="{esq}" y="{alt - 14}" font-size="11" fill="#475569">A Rodada 2 terminou {antecipou} dias '
                  f'antes da data da baseline ({r2["base_fim"]} → {r2["fim"]}); a Rodada 3 começou em seguida e manteve '
                  f'a entrega final em {ENTREGAS[-1]["fim"]}.</text>')
    return f"""
    <div class="diagram-box">
      <div class="diagram-title">Linha do Tempo das Entregas — Baseline × Realizado</div>
      <svg viewBox="0 0 {larg} {alt}" width="100%" height="auto" xmlns="http://www.w3.org/2000/svg"
           font-family="'Segoe UI', -apple-system, 'Helvetica Neue', sans-serif">
        {''.join(partes)}
      </svg>
    </div>
    """


def capitulo_visao_geral():
    linhas = []
    for e in ENTREGAS:
        caps = sorted({capitulo_do_doc(d) for d in e["docs"]} - {None})
        links = ", ".join(f'<a href="#cap-{n}">{n}</a>' for n in caps)
        linhas.append(
            f"<tr><td style=\"white-space:nowrap\"><strong>{e['nome']}</strong><br>{e['demo']}</td>"
            f"<td style=\"white-space:nowrap\">{e['inicio']} a<br>{e['fim']}/{ANO_PROJETO}</td>"
            f"<td>{e['entregou']}</td>"
            f"<td style=\"text-align:center\">{e['horas_prev']} h / {e['horas_real']} h</td>"
            f"<td style=\"text-align:center\">{e['spi']} / {e['cpi']}</td>"
            f"<td style=\"text-align:center; white-space:nowrap\">"
            + "<br>".join(f"<code>{t}</code>" for t in e["tags"].split(" / ")) + "</td>"
            f"<td style=\"text-align:center\">{links}</td></tr>"
        )
    total_prev = sum(e["horas_prev"] for e in ENTREGAS)
    total_real = sum(e["horas_real"] for e in ENTREGAS)
    return f"""
    <p>O projeto foi executado em três rodadas, cada uma encerrada com uma demonstração do produto.
    A tabela resume o período, o que foi entregue e o esforço de cada uma; os gráficos mostram o
    projeto inteiro numa mesma escala de tempo. Os números vêm dos capítulos de monitoramento (EVM e
    burndown) e de registro de participação.</p>
    <table>
      <thead><tr><th>Entrega</th><th>Período</th><th>O que foi entregue</th><th>Horas prev. / reais</th>
      <th>SPI / CPI acumulados</th><th>Tag no Git</th><th>Capítulos</th></tr></thead>
      <tbody>
        {''.join(linhas)}
        <tr><td><strong>Total</strong></td><td>01/09 a 28/10/{ANO_PROJETO}</td><td>RF01 a RF05</td>
        <td style="text-align:center"><strong>{total_prev} h / {total_real} h</strong></td><td></td><td></td><td></td></tr>
      </tbody>
    </table>
    {svg_burndown_geral()}
    {svg_linha_do_tempo()}
    {svg_horas_por_entrega()}
    """


def capitulo_do_doc(doc):
    for numero, (_, _, docs, _) in enumerate(CAPITULOS, start=1):
        if doc in docs:
            return numero
    return None


# Capitulos do documento, na ordem de impressao. O sumario e a numeracao saem desta lista.
# (titulo do capitulo, linha do sumario, documentos em docs/, grafico inserido antes do texto)
CAPITULOS = [
    ("Visão Geral das Entregas (Rodadas 1, 2 e 3)",
     "Visão Geral das Três Entregas (Períodos, Escopo, Horas e Burndown Geral)",
     [], capitulo_visao_geral),
    ("Documento de Visão e Requisitos",
     "Documento de Visão e Requisitos (RF01 a RF05 e RNF01 a RNF05)",
     ["rodada1/01-documento-visao.md"], None),
    ("Backlog do Produto e Priorização",
     "Backlog do Produto e User Stories Refinadas",
     ["rodada2/01-backlog.md"], None),
    ("Escopo do Projeto e Estrutura Analítica (EAP)",
     "Escopo do Projeto e Diagrama Hierárquico da EAP (WBS)",
     ["rodada2/02-escopo-eap.md"], svg_eap_diagram),
    ("Estimativas de Esforço (Planning Poker)",
     "Estimativas de Esforço (Planning Poker e Story Points)",
     ["rodada1/04-estimativas-planning-poker.md"], None),
    ("Cronograma Detalhado e Linha do Tempo (Gantt)",
     "Cronograma Detalhado (Gantt) com Análise de Prazos e Atrasos",
     ["plano-projeto/secao-5-cronograma-orcamento.md"], svg_gantt_diagram),
    ("Monitoramento e Controle (EVM, Curva S e Burndowns)",
     "Monitoramento e Controle (EVM Acumulado, Curva S, Burndowns e Orçamento por EAP e por Membro)",
     ["rodada3/04-monitoramento-consolidado.md"], lambda: svg_curva_s() + svg_burndowns()),
    ("Gestão e Análise de Riscos",
     "Gestão de Riscos (Matriz de Riscos e Planos de Mitigação)",
     ["rodada2/analise-riscos-atualizada.md"], None),
    ("Decisões Técnicas e Arquitetura (ADRs &amp; RDT-01)",
     "Decisões Técnicas e Arquitetura (ADR 001, ADR 002 e RDT-01 Docker/Postgres)",
     ["rodada3/03-dificuldades-decisoes.md", "rodada1/10-decisoes-tecnicas.md", "adr/RDT-01.md"], None),
    ("Gerência de Configuração e Controle de Versões",
     "Gerência de Configuração, Branches e Auditoria do Repositório",
     ["plano-projeto/secao-8-controle-versoes.md"], None),
    ("Histórico de Versões e Entregas",
     "Histórico de Versões, Tags e Integração das Entregas Técnicas",
     ["rodada3/02-historico-versoes.md"], None),
    ("Demonstração do Produto (Demo v3) e Deploy Cloud",
     "Roteiro da Demonstração v3, Publicação em Nuvem e Conta de Teste",
     ["rodada3/01-roteiro-demo-v3.md", "rodada3/deploy-conta-teste.md"], None),
    ("Registro de Participação Individual e Esforço",
     "Registro de Participação Individual e Esforço Consolidado (Horas e Commits)",
     ["rodada3/05-registro-participacao-consolidado.md"], None),
    ("Retrospectiva Final e Encerramento",
     "Retrospectiva Final (Roteiro em Vídeo, Lições Aprendidas e Conclusão)",
     ["rodada3/06-retrospectiva-final.md"], None),
]

# Linha que comeca lista ou tabela em Markdown
INICIO_LISTA_OU_TABELA = re.compile(r"\s*([-*+] |\d+[.)] |\|)")
TITULO_MD = re.compile(r"^(#{1,5})(\s)")


def _paragrafo(linha):
    """Linha de texto corrido (nao e titulo, lista, tabela, citacao, codigo nem HTML)."""
    texto = linha.strip()
    return (bool(texto) and linha[:1] not in " \t"
            and not re.match(r"([-*+] |\d+[.)] |\||#|>|```|<)", texto))


def preparar_markdown(texto):
    """Ajusta o Markdown dos docs para o Python-Markdown renderizar como o GitHub.

    - Remove os blocos Mermaid: os graficos sao desenhados pelos SVGs deste script.
    - Insere linha em branco antes de lista ou tabela colada num paragrafo
      (o GitHub aceita, o Python-Markdown juntaria tudo num paragrafo so).
    - Desce os titulos um nivel: o h1 do documento e o titulo do capitulo.
    """
    saida, em_mermaid, em_codigo = [], False, False
    for linha in texto.splitlines():
        marca = linha.strip()
        if em_mermaid:
            em_mermaid = not marca.startswith("```")
            continue
        if marca.startswith("```mermaid"):
            em_mermaid = True
            continue
        if marca.startswith("```"):
            em_codigo = not em_codigo
        elif not em_codigo:
            if saida and INICIO_LISTA_OU_TABELA.match(linha) and _paragrafo(saida[-1]):
                saida.append("")
            linha = TITULO_MD.sub(r"#\1\2", linha)
        saida.append(linha)
    return "\n".join(saida)


def ajustar_links(html, doc_rel, ancoras):
    """Links relativos entre documentos viram links internos do PDF (capitulo) ou do GitHub."""
    base = posixpath.join("docs", posixpath.dirname(doc_rel))

    def trocar(m):
        href = m.group(1)
        if not href or href.startswith("#") or re.match(r"[a-zA-Z][a-zA-Z0-9+.-]*:", href):
            return m.group(0)
        caminho, _, fragmento = href.partition("#")
        alvo = posixpath.normpath(posixpath.join(base, caminho))
        if alvo in ancoras:
            return f'href="#{ancoras[alvo]}"'
        return f'href="{REPO_URL}/blob/main/{alvo}' + (f"#{fragmento}" if fragmento else "") + '"'

    return re.sub(r'href="([^"]*)"', trocar, html)


def montar_capitulos():
    ancoras = {}
    for numero, (_, _, docs, _) in enumerate(CAPITULOS, start=1):
        for doc in docs:
            ancoras.setdefault(posixpath.join("docs", doc), f"cap-{numero}")

    md = markdown.Markdown(extensions=["tables", "fenced_code", "toc", "sane_lists", "nl2br"])
    sumario, corpo = [], []
    for numero, (titulo, linha_sumario, docs, grafico) in enumerate(CAPITULOS, start=1):
        sumario.append(f'<li><a href="#cap-{numero}"><strong>Seção {numero}.</strong> {linha_sumario}</a></li>')
        partes = [grafico()] if grafico else []
        for doc in docs:
            md.reset()
            html = ajustar_links(md.convert(preparar_markdown(read_doc(doc))), doc, ancoras)
            partes.append(html.replace("R$ ", "R$&nbsp;"))  # valor em reais nao quebra de linha
        corpo.append(
            f'  <div class="chapter" id="cap-{numero}">\n'
            f'    <h1>{numero}. {titulo}</h1>\n' + "\n".join(partes) + "\n  </div>"
        )
    return "\n        ".join(sumario), "\n\n".join(corpo)


CSS = """
    @page {
      size: A4;
      margin: 20mm 15mm 20mm 15mm;
      @bottom-right {
        content: counter(page);
        font-family: 'Segoe UI', -apple-system, 'Helvetica Neue', sans-serif;
        font-size: 9pt;
        color: #64748b;
      }
      @bottom-left {
        content: "FinApp — Projeto de Gerência de Projetos (UFF)";
        font-family: 'Segoe UI', -apple-system, 'Helvetica Neue', sans-serif;
        font-size: 9pt;
        color: #64748b;
      }
    }

    body {
      font-family: 'Segoe UI', -apple-system, BlinkMacSystemFont, 'Helvetica Neue', Roboto, Helvetica, Arial, sans-serif;
      color: #1e293b;
      line-height: 1.55;
      font-size: 10.5pt;
      background: #ffffff;
      margin: 0;
      padding: 0;
    }

    /* Capa Executiva */
    .cover-page {
      page-break-after: always;
      height: 92vh;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      border: 3px solid #1e3a8a;
      padding: 40px;
      box-sizing: border-box;
      background: linear-gradient(180deg, #f8fafc 0%, #ffffff 100%);
    }
    .cover-header {
      text-align: center;
      border-bottom: 2px solid #cbd5e1;
      padding-bottom: 20px;
    }
    /* A capa usa div em vez de h1-h4 para nao entrar nos marcadores do PDF */
    .cover-instituicao {
      margin: 0;
      color: #475569;
      font-size: 13pt;
      font-weight: 700;
      text-transform: uppercase;
      letter-spacing: 1px;
    }
    .cover-subtitulo {
      margin: 5px 0 0 0;
      color: #64748b;
      font-size: 11pt;
      font-weight: 500;
    }
    .cover-title-box {
      text-align: center;
      margin: auto 0;
    }
    .cover-titulo {
      font-size: 28pt;
      line-height: 1.3;
      color: #1e3a8a;
      margin: 0 0 10px 0;
      padding-bottom: 8px;
      border-bottom: 2px solid #e2e8f0;
      font-weight: 800;
      letter-spacing: -0.5px;
    }
    .cover-descricao {
      font-size: 15pt;
      color: #0d9488;
      margin: 0 0 20px 0;
      padding-left: 8px;
      border-left: 4px solid #0d9488;
      font-weight: 600;
    }
    .cover-badge {
      display: inline-block;
      background: #1e3a8a;
      color: #ffffff;
      padding: 6px 18px;
      border-radius: 20px;
      font-size: 10pt;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }
    .cover-footer {
      border-top: 2px solid #cbd5e1;
      padding-top: 20px;
      display: flex;
      justify-content: space-between;
      gap: 16px;
      font-size: 9.5pt;
    }
    .cover-team {
      line-height: 1.6;
    }
    .cover-team strong {
      color: #1e3a8a;
    }

    /* Estrutura de Capitulos */
    .chapter {
      page-break-before: always;
      margin-top: 10px;
    }
    .chapter:first-of-type {
      page-break-before: avoid;
    }

    h1 {
      color: #1e3a8a;
      font-size: 18pt;
      border-bottom: 2px solid #e2e8f0;
      padding-bottom: 8px;
      margin-top: 20px;
      margin-bottom: 15px;
      font-weight: 700;
    }
    h2 {
      color: #0f766e;
      font-size: 13.5pt;
      margin-top: 18px;
      margin-bottom: 10px;
      font-weight: 600;
      border-left: 4px solid #0d9488;
      padding-left: 8px;
    }
    h3 {
      color: #334155;
      font-size: 11.5pt;
      margin-top: 14px;
      margin-bottom: 6px;
      font-weight: 600;
    }
    h4, h5, h6 {
      color: #475569;
      font-size: 10.5pt;
      margin-top: 12px;
      margin-bottom: 5px;
      font-weight: 600;
    }
    /* Titulo nunca fica sozinho no fim da pagina */
    h1, h2, h3, h4, h5, h6 {
      break-after: avoid;
    }

    a {
      color: #1d4ed8;
      text-decoration: none;
    }

    p, ul, ol {
      margin-top: 0;
      margin-bottom: 10px;
      text-align: justify;
    }

    /* Tabelas */
    table {
      width: 100%;
      border-collapse: collapse;
      margin: 14px 0;
      font-size: 9pt;
    }
    /* Tabela longa pode continuar na pagina seguinte, mas sem partir linhas; o cabecalho se repete */
    thead {
      display: table-header-group;
    }
    tr {
      break-inside: avoid;
    }
    th {
      background-color: #1e3a8a;
      color: #ffffff;
      text-align: left;
      padding: 7px 9px;
      font-weight: 600;
      border: 1px solid #1e3a8a;
    }
    td {
      padding: 6px 9px;
      border: 1px solid #cbd5e1;
      vertical-align: middle;
    }
    tr:nth-child(even) {
      background-color: #f8fafc;
    }

    /* Caixas de Destaque e Diagramas */
    .diagram-box {
      border: 1px solid #cbd5e1;
      background: #ffffff;
      border-radius: 8px;
      padding: 15px;
      margin: 18px 0;
      page-break-inside: avoid;
      box-shadow: 0 1px 3px rgba(0,0,0,0.05);
    }
    .diagram-title {
      font-size: 11pt;
      font-weight: 700;
      color: #1e3a8a;
      text-align: center;
      margin-bottom: 12px;
      border-bottom: 1px solid #f1f5f9;
      padding-bottom: 6px;
    }

    blockquote {
      background: #f0fdf4;
      border-left: 4px solid #10b981;
      margin: 12px 0;
      padding: 8px 14px;
      color: #166534;
      font-size: 9.5pt;
    }

    code {
      background-color: #f1f5f9;
      padding: 2px 5px;
      border-radius: 4px;
      font-family: 'SF Mono', Menlo, Consolas, 'Courier New', monospace;
      font-size: 9pt;
      color: #0f172a;
    }
    pre {
      background-color: #0f172a;
      color: #f8fafc;
      padding: 10px;
      border-radius: 6px;
      overflow-x: auto;
      font-size: 8.5pt;
      page-break-inside: avoid;
    }

    /* Sumario */
    .toc-box {
      background: #f8fafc;
      border: 1px solid #e2e8f0;
      border-radius: 8px;
      padding: 20px 30px;
      margin: 20px 0;
    }
    .toc-box ul {
      list-style-type: none;
      padding-left: 0;
      margin-bottom: 0;
    }
    .toc-box li {
      padding: 5px 0;
      border-bottom: 1px dotted #cbd5e1;
      font-size: 10.5pt;
    }
    .toc-box li a {
      text-decoration: none;
      color: #1e3a8a;
      font-weight: 600;
    }
  """


def build_html(data_consolidacao):
    print("Carregando e integrando documentos...")
    sumario, capitulos = montar_capitulos()

    html_content = f"""<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>FinApp — Documento Consolidado do Projeto</title>
  <style>{CSS}</style>
</head>
<body>

  <!-- CAPA -->
  <div class="cover-page">
    <div class="cover-header">
      <div class="cover-instituicao">Universidade Federal Fluminense — UFF</div>
      <div class="cover-subtitulo">Instituto de Computação · Gerência de Projetos e Manutenção de Software (GPMS)</div>
      <div class="cover-subtitulo">Professora: Rebeca Campos Motta · Semestre 2026.2</div>
    </div>

    <div class="cover-title-box">
      <div class="cover-titulo">FinApp — Gestão Financeira Pessoal</div>
      <div class="cover-descricao">Plano de Projeto Consolidado e Relatório de Encerramento</div>
      <div class="cover-badge">Entrega Final · Rodadas 1, 2 e 3 Integradas</div>
    </div>

    <div class="cover-footer">
      <div class="cover-team">
        <strong>Equipe de Desenvolvimento e Gestão:</strong><br>
        • <strong>Filipe Moreira</strong> — Gerente de Projeto (GP) &amp; Dev<br>
        • <strong>Sara Marcomini</strong> — Product Owner (PO)<br>
        • <strong>Emanuel</strong> — Scrum Master<br>
        • <strong>Arben Tafili</strong> — Gerente de Configuração<br>
        • <strong>Enzo</strong> — Desenvolvedor (Infra/DB/Fullstack)<br>
        • <strong>Giovana</strong> — Desenvolvedora (Backend/Frontend)
      </div>
      <div style="text-align: right; line-height: 1.6;">
        <strong>Data de Consolidação:</strong> {data_consolidacao}<br>
        <strong>Status:</strong> 100% Entregue &amp; Homologado<br>
        <strong>Baseline Global:</strong> 153 SP · 612 h · R$ 30.600<br>
        <strong>Repositório:</strong> <a href="{REPO_URL}">GitHub (branch main)</a>
      </div>
    </div>
  </div>

  <!-- SUMARIO (gerado a partir de CAPITULOS) -->
  <div class="chapter">
    <h1>Sumário Executivo do Projeto</h1>
    <div class="toc-box">
      <ul>
        {sumario}
      </ul>
    </div>
  </div>

{capitulos}

</body>
</html>
"""

    with open(OUTPUT_HTML, "w", encoding="utf-8") as f:
        f.write(html_content)
    print(f"HTML consolidado gerado em: {OUTPUT_HTML}")


def encontrar_navegador(indicado=None):
    """Procura um navegador Chromium (Chrome, Edge, Chromium ou Brave) instalado."""
    indicado = indicado or os.environ.get("PDF_BROWSER")
    if indicado:
        caminho = indicado if os.path.isfile(indicado) else shutil.which(indicado)
        if not caminho:
            sys.exit(f"Navegador indicado não encontrado: {indicado}")
        return caminho

    candidatos = []
    sistema = platform.system()
    if sistema == "Darwin":
        apps = ["Google Chrome", "Microsoft Edge", "Chromium", "Brave Browser"]
        for raiz in ("/Applications", os.path.expanduser("~/Applications")):
            candidatos += [os.path.join(raiz, f"{app}.app", "Contents", "MacOS", app) for app in apps]
    elif sistema == "Windows":
        for var in ("PROGRAMFILES(X86)", "PROGRAMFILES", "LOCALAPPDATA"):
            raiz = os.environ.get(var)
            if raiz:
                candidatos += [os.path.join(raiz, "Microsoft", "Edge", "Application", "msedge.exe"),
                               os.path.join(raiz, "Google", "Chrome", "Application", "chrome.exe")]
    for nome in ("google-chrome", "google-chrome-stable", "chromium", "chromium-browser",
                 "microsoft-edge", "msedge", "chrome", "brave-browser"):
        candidatos.append(shutil.which(nome))

    return next((c for c in candidatos if c and os.path.isfile(c)), None)


def gerar_pdf(navegador):
    print(f"Gerando PDF com {os.path.basename(navegador)} (headless)...")
    # Perfil temporario: nao interfere num navegador aberto e nao deixa rastros no perfil do usuario
    with tempfile.TemporaryDirectory(prefix="finapp-pdf-", ignore_cleanup_errors=True) as temp:
        pdf_temp = os.path.join(temp, "consolidado.pdf")
        cmd = [
            navegador,
            "--headless",
            "--disable-gpu",
            "--no-first-run",
            "--no-default-browser-check",
            f"--user-data-dir={os.path.join(temp, 'perfil')}",
            "--no-pdf-header-footer",           # sem data/URL do navegador; o rodape vem do CSS
            "--generate-pdf-document-outline",  # marcadores (sumario lateral) no leitor de PDF
            "--run-all-compositor-stages-before-draw",
            f"--print-to-pdf={pdf_temp}",
            Path(OUTPUT_HTML).as_uri(),
        ]
        try:
            res = subprocess.run(cmd, capture_output=True, text=True, encoding="utf-8",
                                 errors="replace", timeout=180)
            erro = res.stderr.strip()
        except subprocess.TimeoutExpired:
            erro = "o navegador não terminou em 3 minutos"

        if not (os.path.exists(pdf_temp) and os.path.getsize(pdf_temp) > 1000):
            print("Erro ao gerar o PDF:", erro or "o navegador não criou o arquivo")
            return False
        with open(pdf_temp, "rb") as f:
            if f.read(4) != b"%PDF":
                print("Erro ao gerar o PDF: o arquivo criado não é um PDF válido")
                return False

        shutil.copyfile(pdf_temp, OUTPUT_PDF)
        shutil.copyfile(pdf_temp, OUTPUT_PDF_DOCS)

    print("PDF consolidado gerado com sucesso!")
    print(f"1. Raiz: {OUTPUT_PDF} ({os.path.getsize(OUTPUT_PDF)} bytes)")
    print(f"2. Docs: {OUTPUT_PDF_DOCS}")
    return True


def main():
    parser = argparse.ArgumentParser(description="Gera o PDF consolidado do FinApp a partir de docs/.")
    parser.add_argument("--data", default=datetime.date.today().strftime("%d/%m/%Y"),
                        help="data de consolidação mostrada na capa (padrão: hoje, DD/MM/AAAA)")
    parser.add_argument("--navegador",
                        help="caminho ou nome do Chrome/Edge/Chromium (padrão: detecta; ou variável PDF_BROWSER)")
    parser.add_argument("--so-html", action="store_true",
                        help="gera só o HTML intermediário, sem o PDF")
    args = parser.parse_args()

    build_html(args.data)
    if args.so_html:
        return 0

    navegador = encontrar_navegador(args.navegador)
    if not navegador:
        print("Nenhum Chrome, Edge, Chromium ou Brave encontrado. Instale um deles ou indique o "
              "caminho com --navegador (ou a variável PDF_BROWSER).")
        return 1
    return 0 if gerar_pdf(navegador) else 1


if __name__ == "__main__":
    sys.exit(main())
